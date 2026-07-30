/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.service.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.*;
import colesico.framework.service.BeanField;
import colesico.framework.service.Aggregate;
import colesico.framework.service.InjectParam;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.model.teleapi.*;

import javax.lang.model.element.Modifier;

import static colesico.framework.assist.StringUtils.isBlank;

public final class TeleServiceParser extends FrameworkAbstractParser {

    private final ServiceProcessorContext context;

    public TeleServiceParser(ServiceProcessorContext context) {
        super(context.processingEnv());
        this.context = context;
    }

    private void parseBeanFieldParam(TeleCommandElement teleCommand,
                                     ServiceParameterElement param,
                                     AnnotationAssist<BeanField> beanAnn,
                                     AnnotationAssist<BeanField> methodBeanAnn) {

        String fieldName = "";
        String paramBeanName = BeanField.DEFAULT_BEAN;

        if (beanAnn != null) {
            fieldName = beanAnn.unwrap().value();
            paramBeanName = beanAnn.unwrap().bean();
        }

        if (isBlank(fieldName)) {
            fieldName = param.originParameter().name();
        }

        if (paramBeanName.equals(BeanField.DEFAULT_BEAN) && methodBeanAnn != null) {
            paramBeanName = methodBeanAnn.unwrap().bean();
        }

        TeleBeanFieldElement beanParam = new TeleBeanFieldElement(teleCommand, param, fieldName);

        TeleBeanElement bean = teleCommand.getOrCreateParamBean(paramBeanName);
        bean.addField(beanParam);
        teleCommand.addParameter(beanParam);

        context.modulatorKit().notifyTeleParameterParsed(beanParam);
    }

    private TeleAggregateElement parseAggregate(ClassElement aggregateClass) {
        TeleAggregateElement aggregate = new TeleAggregateElement(aggregateClass);
        var fieldList = aggregateClass.fieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        for (var field : fieldList) {
            AnnotationAssist<Aggregate> aggAnn = field.annotation(Aggregate.class);
            if (aggAnn == null) {
                TeleAggregateFieldElement aggField = new TeleAggregateFieldElement(field);
                aggregate.addField(aggField);
            } else {
                var subAggregate = parseAggregate(field.asClassType().asClassElement());
                aggregate.addAggregate(subAggregate);
            }
        }

        return aggregate;
    }

    private void parseAggregateParam(TeleCommandElement teleCommand,
                                     ServiceParameterElement serviceParam,
                                     AnnotationAssist<Aggregate> aggregateAnn,
                                     AnnotationAssist<Aggregate> methodAggregateAnn) {

        ClassElement paramClass = serviceParam.originParameter().asClassType().asClassElement();
        var aggregate = parseAggregate(paramClass);
        TeleAggregateParamElement aggregateParam = new TeleAggregateParamElement(teleCommand, serviceParam, aggregate);
        teleCommand.addParameter(aggregateParam);
        context.modulatorKit().notifyTeleParameterParsed(aggregateParam);
    }


    private void parseInjectParam(TeleCommandElement teleCommand,
                                  ServiceParameterElement serviceParam,
                                  AnnotationAssist<InjectParam> injectParamAnn
    ) {
        TeleInjectParamElement injectParam = new TeleInjectParamElement(teleCommand, serviceParam);
        teleCommand.addParameter(injectParam);
        context.modulatorKit().notifyTeleParameterParsed(injectParam);
    }

    private void parseOrdinaryParam(TeleCommandElement teleCommand, ServiceParameterElement serviceParam) {
        // Process simple param
        TeleOrdinaryParamElement parameter = new TeleOrdinaryParamElement(teleCommand, serviceParam);
        teleCommand.addParameter(parameter);
        context.modulatorKit().notifyTeleParameterParsed(parameter);
    }

    private void parseTeleParameter(TeleCommandElement teleCommand, ServiceParameterElement serviceParam) {

        var originParam = serviceParam.originParameter();

        AnnotationAssist<BeanField> beanAnn = originParam.annotation(BeanField.class);
        AnnotationAssist<BeanField> methodBeanAnn = teleCommand.serviceMethod().originMethod().annotation(BeanField.class);

        AnnotationAssist<Aggregate> aggregateAnn = originParam.annotation(Aggregate.class);
        AnnotationAssist<Aggregate> methodAggregateAnn = teleCommand.serviceMethod().originMethod().annotation(Aggregate.class);

        AnnotationAssist<InjectParam> injectParamAnn = originParam.annotation(InjectParam.class);

        if (beanAnn != null || methodBeanAnn != null) {
            // Check bean params support
            if (!teleCommand.parentTeleService().paramBeans()) {
                throw CodegenException.of()
                        .message("Bean parameters not supported by tele-facade " + teleCommand.parentTeleService().teleType().getCanonicalName())
                        .element(originParam.unwrap())
                        .build();
            } else {
                parseBeanFieldParam(teleCommand, serviceParam, beanAnn, methodBeanAnn);
            }
        } else if (aggregateAnn != null || methodAggregateAnn != null) {
            // Check combined params support
            if (!teleCommand.parentTeleService().aggregates()) {
                throw CodegenException.of()
                        .message("Combined parameters not supported by tele-facade " + teleCommand.parentTeleService().teleType().getCanonicalName())
                        .element(originParam.unwrap())
                        .build();
            } else {
                parseAggregateParam(teleCommand, serviceParam, aggregateAnn, methodAggregateAnn);
            }
        } else if (injectParamAnn != null) {
            parseInjectParam(teleCommand, serviceParam, injectParamAnn);
        } else {
            parseOrdinaryParam(teleCommand, serviceParam);
        }
    }

    private void parseTeleCommandParams(TeleCommandElement teleCommand) {
        var method = teleCommand.serviceMethod();
        for (var param : method.parameters()) {
            parseTeleParameter(teleCommand, param);
        }
    }

    private void parseTeleCommands(TeleServiceElement teleService) {
        ServiceElement service = teleService.parentService();
        for (ServiceMethodElement serviceMethod : service.serviceMethods()) {
            if (serviceMethod.isLocal()) {
                continue;
            }

            TeleCommandElement teleCommand = new TeleCommandElement(serviceMethod);
            teleService.addTeleCommand(teleCommand);
            context.modulatorKit().notifyBeforeParseTeleCommand(teleCommand);
            parseTeleCommandParams(teleCommand);
            context.modulatorKit().notifyTeleCommandParsed(teleCommand);
        }
    }

    /**
     * Perform tele-facades parsing
     */
    public void parse(ServiceElement service) {
        context.modulatorKit().notifyInitTeleFacade(service);
        TeleServiceElement teleService = service.teleService();
        if (teleService == null) {
            return;
        }
        context.modulatorKit().notifyBeforeParseTeleService(teleService);
        parseTeleCommands(teleService);
        context.modulatorKit().notifyTeleFacadeParsed(teleService);
    }

}
