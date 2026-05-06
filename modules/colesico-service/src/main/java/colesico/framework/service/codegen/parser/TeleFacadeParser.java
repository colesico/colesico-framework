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
import colesico.framework.service.BatchField;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.model.teleapi.*;
import org.apache.commons.lang3.StringUtils;

public final class TeleFacadeParser extends FrameworkAbstractParser {

    private final ServiceProcessorContext context;

    public TeleFacadeParser(ServiceProcessorContext context) {
        super(context.processingEnv());
        this.context = context;
    }

    private void parseBatchParam(TeleCommandElement teleCommand,
                                 ServiceParameterElement param,
                                 AnnotationAssist<BatchField> paramBatchAnn,
                                 AnnotationAssist<BatchField> methodBatchAnn) {

        String fieldName = "";
        String batchName = BatchField.DEFAULT_BATCH;

        if (paramBatchAnn != null) {
            fieldName = paramBatchAnn.unwrap().value();
            batchName = paramBatchAnn.unwrap().batch();
        }

        if (StringUtils.isBlank(fieldName)) {
            fieldName = param.originParameter().name();
        }

        if (batchName.equals(BatchField.DEFAULT_BATCH) && methodBatchAnn != null) {
            batchName = methodBatchAnn.unwrap().batch();
        }

        TeleBatchParamElement batchField = new TeleBatchParamElement(teleCommand, param, fieldName);

        TeleBatchElement batch = teleCommand.getOrCreateBatch(batchName);
        batch.addField(batchField);
        teleCommand.addParameter(batchField);

        context.modulatorKit().notifyTeleParameterParsed(batchField);
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

            AnnotationAssist<BatchField> paramBatchAnn = param.originParameter().annotation(BatchField.class);
            AnnotationAssist<BatchField> methodBatchAnn = teleCommand.serviceMethod().originMethod().annotation(BatchField.class);

            if (paramBatchAnn != null || methodBatchAnn != null) {
                // Check batch support
                if (!teleCommand.parentTeleFacade().batchParams()) {
                    throw CodegenException.of()
                            .message("Batch parameters not supported by tele-facade " + teleCommand.parentTeleFacade().teleType().getCanonicalName())
                            .element(param.originParameter().unwrap())
                            .build();
                } else {
                    parseBatchParam(teleCommand, param, paramBatchAnn, methodBatchAnn);
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

    private void parseTeleCommands(TeleFacadeElement teleFacade) {
        ServiceElement service = teleFacade.parentService();
        for (ServiceMethodElement serviceMethod : service.serviceMethods()) {
            if (serviceMethod.isLocal()) {
                continue;
            }

            TeleCommandElement teleCommand = new TeleCommandElement(serviceMethod);
            teleFacade.addTeleCommand(teleCommand);
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
        TeleFacadeElement teleFacade = service.teleFacade();
        if (teleFacade == null) {
            return;
        }
        context.modulatorKit().notifyBeforeParseTeleFacade(teleFacade);
        parseTeleCommands(teleFacade);
        context.modulatorKit().notifyTeleFacadeParsed(teleFacade);
    }

}
