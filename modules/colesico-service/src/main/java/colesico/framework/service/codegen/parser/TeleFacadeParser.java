/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
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
import colesico.framework.service.Compound;
import colesico.framework.service.LocalField;
import colesico.framework.service.codegen.model.*;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.Iterator;
import java.util.List;

public final class TeleFacadeParser extends FrameworkAbstractParser {

    private final ServiceProcessorContext context;

    public TeleFacadeParser(ServiceProcessorContext context) {
        super(context.getProcessingEnv());
        this.context = context;
    }

    private void parseCompound(TeleMethodElement teleMethod, TeleCompoundElement parentCompound, VarElement variable) {
        if (variable.asClassType() == null) {
            throw CodegenException.of().message("Unsupported type kind for tele-compound " + variable.asClassType().asClassElement().getName() + " " + variable.getName())
                    .element(variable.unwrap())
                    .build();
        }

        TeleCompoundElement compound = new TeleCompoundElement(teleMethod, variable);

        if (parentCompound == null) {
            teleMethod.addParameter(compound);
        } else {
            // Check recursive objects
            Iterator<TeleCompoundElement> it = (Iterator<TeleCompoundElement>) parentCompound.getIterator();
            while (it.hasNext()) {
                TeleCompoundElement curComp = it.next();
                if (curComp.equals(compound)) {
                    TeleFacadeElement teleFacade = teleMethod.getParentTeleFacade();
                    throw CodegenException.of().message("Recursive compound for: "
                                    + compound.getOriginElement().getOriginType()
                                    + " "
                                    + compound.getOriginElement().getName()
                                    + " in: "
                                    + teleFacade.getParentService().getOriginClass().getOriginType()
                                    + "->" + teleMethod.getServiceMethod().getName())
                            .element(variable)
                            .build();
                }
            }
            compound.setParentCompound(parentCompound);
        }

        // Parse compound fields
        List<FieldElement> fields = variable.asClassType().asClassElement().getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.FINAL)
                        && !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        parseVariables(teleMethod, compound, fields);

        context.getModulatorKit().notifyTeleInputParsed(compound);
    }

    private void parseBatchField(TeleMethodElement teleMethod, TeleCompoundElement parentCompound, VarElement variable) {
        AnnotationAssist<BatchField> batchAnn = variable.getAnnotation(BatchField.class);
        String fieldName = batchAnn.unwrap().value();
        if (StringUtils.isBlank(fieldName)) {
            fieldName = variable.getName();
        }

        TeleBatchFieldElement batchField = new TeleBatchFieldElement(teleMethod, variable, fieldName);

        //TODO: Batch name from BatchName annotation
        TeleBatchElement batch = teleMethod.getOrCreateBatch("");
        batch.addField(batchField);
        if (parentCompound == null) {
            teleMethod.addParameter(batchField);
        } else {
            parentCompound.addField(batchField);
        }
        context.getModulatorKit().notifyTeleInputParsed(batchField);
    }

    private void parseParameter(TeleMethodElement teleMethod, TeleCompoundElement parentCompound, VarElement variable) {
        // Process simple param
        TeleParameterElement parameter = new TeleParameterElement(teleMethod, variable);
        if (parentCompound == null) {
            teleMethod.addParameter(parameter);
        } else {
            parentCompound.addField(parameter);
        }
        context.getModulatorKit().notifyTeleInputParsed(parameter);
    }

    private void parseVariables(TeleMethodElement teleMethod, TeleCompoundElement parentCompound, List<? extends VarElement> variables) {
        for (VarElement variable : variables) {
            AnnotationAssist<LocalField> localFieldAnn = variable.getAnnotation(LocalField.class);

            // Skip local fields for compounds
            if (parentCompound != null && localFieldAnn != null) {
                continue;
            }

            AnnotationAssist<Compound> compoundAnn = variable.getAnnotation(Compound.class);

            if (compoundAnn == null) {
                AnnotationAssist<BatchField> batchAnn = variable.getAnnotation(BatchField.class);
                if (batchAnn == null) {
                    parseParameter(teleMethod, parentCompound, variable);
                } else {
                    // Check batch support
                    if (!teleMethod.getParentTeleFacade().getBatchParams()) {
                        throw CodegenException.of()
                                .message("Batch parameters not supported by tele-facade " + teleMethod.getParentTeleFacade().getTeleType().getCanonicalName())
                                .element(variable.unwrap())
                                .build();
                    } else {
                        parseBatchField(teleMethod, parentCompound, variable);
                    }
                }

            } else {
                // Check compound support
                if (!teleMethod.getParentTeleFacade().getCompoundParams()) {
                    throw CodegenException.of()
                            .message("Compound parameters not supported by tele-facade " + teleMethod.getParentTeleFacade().getTeleType().getCanonicalName())
                            .element(variable.unwrap())
                            .build();
                }
                parseCompound(teleMethod, parentCompound, variable);
            }
        }
    }

    protected void parseTeleMethodParams(TeleMethodElement teleMethod) {
        MethodElement method = teleMethod.getServiceMethod().getOriginMethod();
        parseVariables(teleMethod, null, method.getParameters());
    }

    protected void parseTeleMethods(TeleFacadeElement teleFacade) {
        ServiceElement service = teleFacade.getParentService();
        for (ServiceMethodElement serviceMethod : service.getServiceMethods()) {
            if (serviceMethod.isLocal()) {
                continue;
            }

            TeleMethodElement teleMethod = new TeleMethodElement(serviceMethod);
            teleFacade.addTeleMethod(teleMethod);
            context.getModulatorKit().notifyBeforeParseTeleMethod(teleMethod);
            parseTeleMethodParams(teleMethod);
            context.getModulatorKit().notifyTeleMethodParsed(teleMethod);
        }
    }

    /**
     * Perform tele-facades parsing
     */
    public void parse(ServiceElement service) {
        context.getModulatorKit().notifyInitTeleFacade(service);
        TeleFacadeElement teleFacade = service.getTeleFacade();
        if (teleFacade == null) {
            return;
        }
        context.getModulatorKit().notifyBeforeParseTeleFacade(teleFacade);
        parseTeleMethods(teleFacade);
        context.getModulatorKit().notifyTeleFacadeParsed(teleFacade);
    }

}
