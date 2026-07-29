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
import colesico.framework.service.FieldParam;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.model.teleapi.*;

import static colesico.framework.assist.StringUtils.isBlank;

public final class TeleServiceParser extends FrameworkAbstractParser {

    private final ServiceProcessorContext context;

    public TeleServiceParser(ServiceProcessorContext context) {
        super(context.processingEnv());
        this.context = context;
    }

    private void parseRequestBeanParam(TeleCommandElement teleCommand,
                                 ServiceParameterElement param,
                                 AnnotationAssist<FieldParam> paramRequestBeanAnn,
                                 AnnotationAssist<FieldParam> methodRequestBeanAnn) {

        String fieldName = "";
        String requestBeanName = FieldParam.DEFAULT_BEAN;

        if (paramRequestBeanAnn != null) {
            fieldName = paramRequestBeanAnn.unwrap().value();
            requestBeanName = paramRequestBeanAnn.unwrap().bean();
        }

        if (isBlank(fieldName)) {
            fieldName = param.originParameter().name();
        }

        if (requestBeanName.equals(FieldParam.DEFAULT_BEAN) && methodRequestBeanAnn != null) {
            requestBeanName = methodRequestBeanAnn.unwrap().bean();
        }

        TeleFieldParamElement requestBeanParam = new TeleFieldParamElement(teleCommand, param, fieldName);

        TeleRequestBeanElement requestBean = teleCommand.getOrCreateRequestBean(requestBeanName);
        requestBean.addField(requestBeanParam);
        teleCommand.addParameter(requestBeanParam);

        context.modulatorKit().notifyTeleParameterParsed(requestBeanParam);
    }

    private void parseInjectParam(TeleCommandElement teleCommand,
                                  ServiceInjectParamElement param
    ) {

        TeleInjectParamElement injectParam = new TeleInjectParamElement(teleCommand, param);
        teleCommand.addParameter(injectParam);
        context.modulatorKit().notifyTeleParameterParsed(injectParam);
    }

    private void parseParameter(TeleCommandElement teleCommand, ServiceParameterElement param) {
        // Process simple param
        TeleOrdinaryParamElement parameter = new TeleOrdinaryParamElement(teleCommand, param);
        teleCommand.addParameter(parameter);
        context.modulatorKit().notifyTeleParameterParsed(parameter);
    }

    private void parseTeleCommandParams(TeleCommandElement teleCommand) {
        var method = teleCommand.serviceMethod();
        for (var param : method.parameters()) {

            AnnotationAssist<FieldParam> paramRequestBeanAnn = param.originParameter().annotation(FieldParam.class);
            AnnotationAssist<FieldParam> methodRequestBeanAnn = teleCommand.serviceMethod().originMethod().annotation(FieldParam.class);

            if (paramRequestBeanAnn != null || methodRequestBeanAnn != null) {
                // Check requestBean support
                if (!teleCommand.parentTeleService().requestBeanParams()) {
                    throw CodegenException.of()
                            .message("RequestBean parameters not supported by tele-facade " + teleCommand.parentTeleService().teleType().getCanonicalName())
                            .element(param.originParameter().unwrap())
                            .build();
                } else {
                    parseRequestBeanParam(teleCommand, param, paramRequestBeanAnn, methodRequestBeanAnn);
                }
            } else {
                if (param instanceof ServiceInjectParamElement injParam) {
                    parseInjectParam(teleCommand, injParam);
                } else {
                    parseParameter(teleCommand, param);
                }
            }
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
