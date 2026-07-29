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
import colesico.framework.service.BundleParam;
import colesico.framework.service.CombinedParams;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.model.teleapi.*;

import static colesico.framework.assist.StringUtils.isBlank;

public final class TeleServiceParser extends FrameworkAbstractParser {

    private final ServiceProcessorContext context;

    public TeleServiceParser(ServiceProcessorContext context) {
        super(context.processingEnv());
        this.context = context;
    }

    private void parseBundleParam(TeleCommandElement teleCommand,
                                  ServiceParameterElement param,
                                  AnnotationAssist<BundleParam> bundleParamAnn,
                                  AnnotationAssist<BundleParam> methodParamBundleAnn) {

        String fieldName = "";
        String paramBundleName = BundleParam.DEFAULT_BUNDLE;

        if (bundleParamAnn != null) {
            fieldName = bundleParamAnn.unwrap().value();
            paramBundleName = bundleParamAnn.unwrap().bundle();
        }

        if (isBlank(fieldName)) {
            fieldName = param.originParameter().name();
        }

        if (paramBundleName.equals(BundleParam.DEFAULT_BUNDLE) && methodParamBundleAnn != null) {
            paramBundleName = methodParamBundleAnn.unwrap().bundle();
        }

        TeleBundleFieldElement bundleParam = new TeleBundleFieldElement(teleCommand, param, fieldName);

        TeleBundleElement paramBundle = teleCommand.getOrCreateParamBundle(paramBundleName);
        paramBundle.addField(bundleParam);
        teleCommand.addParameter(bundleParam);

        context.modulatorKit().notifyTeleParameterParsed(bundleParam);
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

            AnnotationAssist<BundleParam> bundleParamAnn = param.originParameter().annotation(BundleParam.class);
            AnnotationAssist<BundleParam> methodBundleParamAnn = teleCommand.serviceMethod().originMethod().annotation(BundleParam.class);

            AnnotationAssist<CombinedParams> compositeParamAnn = param.originParameter().annotation(CombinedParams.class);
            AnnotationAssist<CombinedParams> methodCompositeParamAnn = teleCommand.serviceMethod().originMethod().annotation(CombinedParams.class);

            if (bundleParamAnn != null || methodBundleParamAnn != null) {
                // Check bundle param support
                if (!teleCommand.parentTeleService().bundleParams()) {
                    throw CodegenException.of()
                            .message("ParamBundle parameters not supported by tele-facade " + teleCommand.parentTeleService().teleType().getCanonicalName())
                            .element(param.originParameter().unwrap())
                            .build();
                } else {
                    parseBundleParam(teleCommand, param, bundleParamAnn, methodBundleParamAnn);
                }
            } else if (compositeParamAnn != null || methodCompositeParamAnn != null) {

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
