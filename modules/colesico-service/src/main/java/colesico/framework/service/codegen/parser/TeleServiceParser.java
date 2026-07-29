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

    private void parseBundleParam(TeleCommandElement teleCommand,
                                  ServiceParameterElement param,
                                  AnnotationAssist<BundleParam> bundleParamAnn,
                                  AnnotationAssist<BundleParam> methodBundleParamAnn) {

        String fieldName = "";
        String paramBundleName = BundleParam.DEFAULT_BUNDLE;

        if (bundleParamAnn != null) {
            fieldName = bundleParamAnn.unwrap().value();
            paramBundleName = bundleParamAnn.unwrap().bundle();
        }

        if (isBlank(fieldName)) {
            fieldName = param.originParameter().name();
        }

        if (paramBundleName.equals(BundleParam.DEFAULT_BUNDLE) && methodBundleParamAnn != null) {
            paramBundleName = methodBundleParamAnn.unwrap().bundle();
        }

        TeleBundleFieldElement bundleParam = new TeleBundleFieldElement(teleCommand, param, fieldName);

        TeleBundleElement paramBundle = teleCommand.getOrCreateParamBundle(paramBundleName);
        paramBundle.addField(bundleParam);
        teleCommand.addParameter(bundleParam);

        context.modulatorKit().notifyTeleParameterParsed(bundleParam);
    }

    private void parseCombinedParams(TeleCommandElement teleCommand,
                                     ServiceParameterElement param,
                                     AnnotationAssist<CombinedParams> combinedParamsAnn,
                                     AnnotationAssist<CombinedParams> methodCombinedParamsAnn) {

        ClassElement paramClass = param.originParameter().asClassType().asClassElement();
        TeleCombinationElement combinedParam = new TeleCombinationElement(teleCommand, param, paramClass);

        var fields = paramClass.fieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );
    }


    private void parseInjectParam(TeleCommandElement teleCommand,
                                  ServiceParameterElement param,
                                  AnnotationAssist<InjectParam> injectParamAnn
    ) {
        TeleInjectParamElement injectParam = new TeleInjectParamElement(teleCommand, param);
        teleCommand.addParameter(injectParam);
        context.modulatorKit().notifyTeleParameterParsed(injectParam);
    }

    private void parseOrdinaryParameter(TeleCommandElement teleCommand, ServiceParameterElement param) {
        // Process simple param
        TeleOrdinaryParamElement parameter = new TeleOrdinaryParamElement(teleCommand, param);
        teleCommand.addParameter(parameter);
        context.modulatorKit().notifyTeleParameterParsed(parameter);
    }

    private void parseTeleParameter(TeleCommandElement teleCommand, ServiceParameterElement serviceParam, VarElement teleParam) {
        AnnotationAssist<BundleParam> bundleParamAnn = teleParam.annotation(BundleParam.class);
        AnnotationAssist<BundleParam> methodBundleParamAnn = teleCommand.serviceMethod().originMethod().annotation(BundleParam.class);

        AnnotationAssist<CombinedParams> combinedParamsAnn = teleParam.annotation(CombinedParams.class);
        AnnotationAssist<CombinedParams> methodCombinedParamsAnn = teleCommand.serviceMethod().originMethod().annotation(CombinedParams.class);

        AnnotationAssist<InjectParam> injectParamAnn = teleParam.annotation(InjectParam.class);

        if (bundleParamAnn != null || methodBundleParamAnn != null) {
            // Check bundle params support
            if (!teleCommand.parentTeleService().bundleParams()) {
                throw CodegenException.of()
                        .message("Bundle parameters not supported by tele-facade " + teleCommand.parentTeleService().teleType().getCanonicalName())
                        .element(teleParam.unwrap())
                        .build();
            } else {
                parseBundleParam(teleCommand, param, bundleParamAnn, methodBundleParamAnn);
            }
        } else if (combinedParamsAnn != null || methodCombinedParamsAnn != null) {
            // Check combined params support
            if (!teleCommand.parentTeleService().combinedParams()) {
                throw CodegenException.of()
                        .message("Combined parameters not supported by tele-facade " + teleCommand.parentTeleService().teleType().getCanonicalName())
                        .element(teleParam.unwrap())
                        .build();
            } else {
                parseCombinedParams(teleCommand, param, combinedParamsAnn, methodCombinedParamsAnn);
            }
        } else if (injectParamAnn != null) {
            parseInjectParam(teleCommand, param, injectParamAnn);
        } else {
            parseOrdinaryParameter(teleCommand, param);
        }
    }

    private void parseTeleCommandParams(TeleCommandElement teleCommand) {
        var method = teleCommand.serviceMethod();
        for (var param : method.parameters()) {
            parseTeleParameter(teleCommand, param, param.originParameter());
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
