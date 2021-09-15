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
import colesico.framework.service.Compound;
import colesico.framework.service.LocalField;
import colesico.framework.service.codegen.model.*;

import javax.lang.model.element.Modifier;
import java.util.Iterator;
import java.util.List;

public final class TeleFacadeParser extends FrameworkAbstractParser {

    private final ServiceProcessorContext context;

    public TeleFacadeParser(ServiceProcessorContext context) {
        super(context.getProcessingEnv());
        this.context = context;
    }

    private void parseTeleCompound(TeleMethodElement teleMethod, TeleCompoundElement parentCompound, VarElement arg) {
        if (arg.asClassType() == null) {
            throw CodegenException.of().message("Unsupported type kind for tele-compound " + arg.asClassType().asClassElement().getName() + " " + arg.getName())
                    .element(arg.unwrap())
                    .build();
        }

        TeleCompoundElement teleComp = new TeleCompoundElement(arg);
        teleComp.setParentTeleMethod(teleMethod);

        if (parentCompound == null) {
            teleMethod.addParameter(teleComp);
        } else {
            // Check recursive objects
            Iterator<TeleCompoundElement> it = (Iterator<TeleCompoundElement>) parentCompound.getIterator();
            while (it.hasNext()) {
                TeleCompoundElement curComp = it.next();
                if (curComp.equals(teleComp)) {
                    TeleFacadeElement teleFacade = teleMethod.getParentTeleFacade();
                    throw CodegenException.of().message("Recursive compound for: "
                                    + teleComp.getOriginElement().getOriginType()
                                    + " "
                                    + teleComp.getOriginElement().getName()
                                    + " in: "
                                    + teleFacade.getParentService().getOriginClass().getOriginType()
                                    + "->" + teleMethod.getServiceMethod().getName())
                            .element(arg)
                            .build();
                }
            }
            teleComp.setParentCompound(parentCompound);
        }

        List<FieldElement> fields = arg.asClassType().asClassElement().getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.FINAL)
                        && !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        parseTeleArguments(teleMethod, teleComp, fields);

    }

    private void parseTeleArguments(TeleMethodElement teleMethod, TeleCompoundElement parentCompound, List<? extends VarElement> arguments) {
        for (VarElement arg : arguments) {
            AnnotationAssist<LocalField> localFieldAnn = arg.getAnnotation(LocalField.class);

            // Skip local fields
            if (localFieldAnn != null ){
                continue;
            }

            AnnotationAssist<Compound> compoundAnn = arg.getAnnotation(Compound.class);

            if (compoundAnn == null) {
                TeleParameterElement teleParam = new TeleParameterElement(arg, teleMethod.nextParamIndex());
                teleParam.setParentTeleMethod(teleMethod);
                if (parentCompound == null) {
                    teleMethod.addParameter(teleParam);
                } else {
                    parentCompound.addField(teleParam);
                    teleParam.setParentCompound(parentCompound);
                }
                context.getModulatorKit().notifyTeleParamParsed(teleParam);
            } else {
                // Check compound support
                if (!teleMethod.getParentTeleFacade().getCompoundSupport()) {
                    throw CodegenException.of()
                            .message("Compound parameters not supported by tele-facade " + teleMethod.getParentTeleFacade().getTeleType().getCanonicalName())
                            .element(arg.unwrap())
                            .build();
                }
                parseTeleCompound(teleMethod, parentCompound, arg);
            }

        }
    }

    protected void parseTeleMethodParams(TeleMethodElement teleMethod) {
        MethodElement method = teleMethod.getServiceMethod().getOriginMethod();
        int paramIndex = 0;
        parseTeleArguments(teleMethod, null, method.getParameters());
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
