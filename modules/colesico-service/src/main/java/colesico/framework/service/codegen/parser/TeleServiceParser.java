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
import colesico.framework.service.ParamsBean;
import colesico.framework.service.InjectParam;
import colesico.framework.service.LocalParam;
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

    private TeleParameterElement parseBeanFieldParameter(TeleCommandElement teleCommand,
                                                         VarElement originParam,
                                                         AnnotationAssist<BeanField> beanAnn,
                                                         AnnotationAssist<BeanField> methodBeanAnn) {

        TeleCompositionFieldElement beanField = new TeleCompositionFieldElement(teleCommand, originParam);

        String fieldName = "";
        String compositionName = BeanField.DEFAULT_BEAN;

        if (beanAnn != null) {
            fieldName = beanAnn.unwrap().value();
            compositionName = beanAnn.unwrap().bean();
        }

        if (isBlank(fieldName)) {
            fieldName = originParam.name();
        }

        beanField.setName(fieldName);

        if (compositionName.equals(BeanField.DEFAULT_BEAN) && methodBeanAnn != null) {
            compositionName = methodBeanAnn.unwrap().bean();
        }

        TeleCompositionElement composition = teleCommand.getOrCreateComposition(compositionName);
        composition.addField(beanField);

        context.modulatorKit().notifyTeleParameterParsed(beanField);

        return beanField;
    }


    private TeleParameterElement parseAggregationParameter(TeleCommandElement teleCommand,
                                                         VarElement parameter,
                                                         AnnotationAssist<ParamsBean> aggregationAnn,
                                                         AnnotationAssist<ParamsBean> methodAggregationAnn) {

        TeleAggregationElement aggregationParam = new TeleAggregationElement(teleCommand, parameter);

        var fieldList = parameter.asClassType().asClassElement().fieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        for (var field : fieldList) {
            aggregationParam.addField(parseTeleParameter(teleCommand, field));
        }

        context.modulatorKit().notifyTeleParameterParsed(aggregationParam);
        return aggregationParam;
    }


    private TeleParameterElement parseInjectParameter(TeleCommandElement teleCommand,
                                                      VarElement parameter,
                                                      AnnotationAssist<InjectParam> injectParamAnn) {
        TeleInjectParamElement injectParam = new TeleInjectParamElement(teleCommand, parameter);
        context.modulatorKit().notifyTeleParameterParsed(injectParam);
        return injectParam;
    }

    private TeleParameterElement parseOrdinaryParameter(TeleCommandElement teleCommand, VarElement parameter) {
        // Process simple param
        TeleOrdinaryParamElement ordinaryParam = new TeleOrdinaryParamElement(teleCommand, parameter);
        context.modulatorKit().notifyTeleParameterParsed(ordinaryParam);
        return ordinaryParam;
    }

    private TeleParameterElement parseTeleParameter(TeleCommandElement teleCommand, VarElement parameter) {

        TeleParameterElement result = null;

        AnnotationAssist<LocalParam> localParamAnn = parameter.annotation(LocalParam.class);

        AnnotationAssist<BeanField> beanAnn = parameter.annotation(BeanField.class);
        AnnotationAssist<BeanField> methodBeanAnn = teleCommand.serviceMethod().originMethod().annotation(BeanField.class);

        AnnotationAssist<ParamsBean> aggregationAnn = parameter.annotation(ParamsBean.class);
        AnnotationAssist<ParamsBean> methodAggregationAnn = teleCommand.serviceMethod().originMethod().annotation(ParamsBean.class);

        AnnotationAssist<InjectParam> injectParamAnn = parameter.annotation(InjectParam.class);

        if (beanAnn != null || methodBeanAnn != null) {
            // Check bean params support
            if (!teleCommand.parentTeleService().supportParamCompositions()) {
                throw CodegenException.of()
                        .message("Bean parameters not supported by tele-facade " + teleCommand.parentTeleService().teleType().getCanonicalName())
                        .element(parameter.unwrap())
                        .build();
            }
            result = parseBeanFieldParameter(teleCommand, parameter, beanAnn, methodBeanAnn);
        } else if (aggregationAnn != null || methodAggregationAnn != null) {
            // Check combined params support
            if (!teleCommand.parentTeleService().supportParamAggregations()) {
                throw CodegenException.of()
                        .message("Aggregation parameters not supported by tele-facade " + teleCommand.parentTeleService().teleType().getCanonicalName())
                        .element(parameter.unwrap())
                        .build();
            }
            result = parseAggregationParameter(teleCommand, parameter, aggregationAnn, methodAggregationAnn);
        } else if (injectParamAnn != null) {
            result = parseInjectParameter(teleCommand, parameter, injectParamAnn);
        } else {
            result = parseOrdinaryParameter(teleCommand, parameter);
        }

        if (localParamAnn != null) {
            result.setLocalParam(true);
        }

        return result;
    }

    private void parseTeleCommandParams(TeleCommandElement teleCommand) {
        var method = teleCommand.serviceMethod();
        for (var param : method.parameters()) {
            var teleParam = parseTeleParameter(teleCommand, param.originParameter());
            teleCommand.addParameter(teleParam);
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
