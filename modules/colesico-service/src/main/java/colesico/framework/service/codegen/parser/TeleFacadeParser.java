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

import java.util.List;

public final class TeleFacadeParser extends FrameworkAbstractParser {

    private final ServiceProcessorContext context;

    public TeleFacadeParser(ServiceProcessorContext context) {
        super(context.processingEnv());
        this.context = context;
    }

    private void parseBatchField(TeleCommandElement teleCommand,
                                 VarElement variable,
                                 AnnotationAssist<BatchField> paramBatchAnn,
                                 AnnotationAssist<BatchField> methodBatchAnn) {

        String fieldName = "";
        String batchName = BatchField.DEFAULT_BATCH;

        if (paramBatchAnn != null) {
            fieldName = paramBatchAnn.unwrap().value();
            batchName = paramBatchAnn.unwrap().batch();
        }

        if (StringUtils.isBlank(fieldName)) {
            fieldName = variable.name();
        }

        if (batchName.equals(BatchField.DEFAULT_BATCH) && methodBatchAnn != null) {
            batchName = methodBatchAnn.unwrap().batch();
        }

        TeleBatchFieldElement batchField = new TeleBatchFieldElement(teleCommand, variable, fieldName);

        TeleBatchElement batch = teleCommand.getOrCreateBatch(batchName);
        batch.addField(batchField);
        teleCommand.addParameter(batchField);

        context.modulatorKit().notifyTeleInputParsed(batchField);
    }

    private void parseParameter(TeleCommandElement teleCommand, VarElement variable) {
        // Process simple param
        TeleParameterElement parameter = new TeleParameterElement(teleCommand, variable);
        teleCommand.addParameter(parameter);
        context.modulatorKit().notifyTeleInputParsed(parameter);
    }

    private void parseVariables(TeleCommandElement teleCommand, List<? extends VarElement> variables) {
        for (VarElement variable : variables) {

            AnnotationAssist<BatchField> paramBatchAnn = variable.annotation(BatchField.class);
            AnnotationAssist<BatchField> methodBatchAnn = teleCommand.serviceMethod().originMethod().annotation(BatchField.class);

            if (paramBatchAnn != null || methodBatchAnn != null) {
                // Check batch support
                if (!teleCommand.parentTeleFacade().batchParams()) {
                    throw CodegenException.of()
                            .message("Batch parameters not supported by tele-facade " + teleCommand.parentTeleFacade().teleType().getCanonicalName())
                            .element(variable.unwrap())
                            .build();
                } else {
                    parseBatchField(teleCommand, variable, paramBatchAnn, methodBatchAnn);
                }
            } else {
                parseParameter(teleCommand, variable);
            }
        }
    }

    private void parseTeleCommandParams(TeleCommandElement teleCommand) {
        MethodElement method = teleCommand.serviceMethod().originMethod();
        parseVariables(teleCommand, method.parameters());
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
