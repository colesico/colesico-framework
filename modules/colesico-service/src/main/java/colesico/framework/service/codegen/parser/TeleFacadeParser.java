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
import colesico.framework.service.codegen.model.*;

public final class TeleFacadeParser extends FrameworkAbstractParser {

    private final ServiceProcessorContext context;

    public TeleFacadeParser(ServiceProcessorContext context) {
        super(context.getProcessingEnv());
        this.context = context;
    }

    protected void parseTeleMethodParams(TeleMethodElement teleMethod) {
        MethodElement method = teleMethod.getServiceMethod().getOriginMethod();
        int paramIndex = 0;
        for (ParameterElement param : method.getParameters()) {
            TeleParamElement teleParam = new TeleParamElement(param, paramIndex);
            teleMethod.addParameter(teleParam);
            context.getModulatorKit().notifyTeleParamParsed(teleParam);
            paramIndex++;
        }
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
