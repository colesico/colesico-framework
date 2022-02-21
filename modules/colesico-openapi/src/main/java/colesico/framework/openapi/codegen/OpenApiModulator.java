/*
 * Copyright © 2014-2021 Vladlen V. Larionov and others as noted.
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

package colesico.framework.openapi.codegen;


import colesico.framework.openapi.InputParam;
import colesico.framework.openapi.OpenApiScheme;
import colesico.framework.restlet.Restlet;
import colesico.framework.restlet.codegen.RestletModulator;
import colesico.framework.restlet.codegen.assist.RestletCodegenUtils;
import colesico.framework.restlet.codegen.model.JsonFieldElement;
import colesico.framework.router.codegen.RoutesBuilder;
import colesico.framework.service.codegen.assist.ServiceCodegenUtils;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParameterElement;
import colesico.framework.service.codegen.model.TeleInputElement;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.teleapi.TeleScheme;
import colesico.framework.service.codegen.modulator.TeleSchemeModulator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;

import javax.lang.model.type.TypeMirror;

/**
 * Modulates Swagger definition
 */
public final class OpenApiModulator extends TeleSchemeModulator {


    @Override
    public ListenOrder listenOrder(Class<? extends Modulator> thatModulator) {

        if (thatModulator.equals(RestletModulator.class)) {
            return ListenOrder.AFTER;
        }

        return ListenOrder.NO_MATTER;
    }

    @Override
    protected boolean isTeleFacadeSupported(TeleFacadeElement teleFacade) {
        return teleFacade != null && teleFacade.getTeleType().equals(Restlet.class);
    }

    @Override
    protected Class<?> getSchemeType() {
        return OpenAPI.class;
    }

    @Override
    protected Class<? extends TeleScheme> getTeleSchemeBaseClass() {
        return OpenApiScheme.class;
    }

    @Override
    protected void processTeleFacade(TeleFacadeElement teleFacade) {
        CodeBlock.Builder cb = CodeBlock.builder();

        // OpenApi api = api(ServiceClassSimpleName);
        cb.addStatement("$T $N = $N($S)",
                ClassName.get(OpenAPI.class),
                OpenApiScheme.API_VAR,
                OpenApiScheme.CREATE_API_METHOD,
                teleFacade.getParentService().getOriginClass().getSimpleName()
        );

        processMethods(cb, teleFacade);

        cb.addStatement("return $N", OpenApiScheme.API_VAR);
        getTeleScheme().setBuildMethodBody(cb.build());
    }


    protected void processMethods(CodeBlock.Builder cb, TeleFacadeElement teleFacade) {
        cb.addStatement("$T $N", ClassName.get(Operation.class), OpenApiScheme.OPERATION_VAR);
        cb.addStatement("$T $N", ClassName.get(InputParam.class), OpenApiScheme.PARAM_VAR);

        for (TeleMethodElement teleMethod : teleFacade.getTeleMethods()) {
            RoutesBuilder.RoutedTeleMethodElement routedTeleMethod = teleMethod.getProperty(RoutesBuilder.RoutedTeleMethodElement.class);
            if (routedTeleMethod == null) {
                continue;
            }

            String path = routedTeleMethod.getRoutePath();
            String httpMethod = routedTeleMethod.getHttpMethodName();

            // operation = operation(api, methodName, path, httpMethod)
            cb.addStatement("$N = $N($N,$S,$S,$S)",
                    OpenApiScheme.OPERATION_VAR,
                    OpenApiScheme.CREATE_OPERATION_METHOD,
                    OpenApiScheme.API_VAR,
                    teleMethod.getServiceMethod().getName(),
                    path,
                    httpMethod
            );

            // generateInputParams(cb, teleMethod);
        }
    }

    protected void generateInputParams(CodeBlock.Builder cb, TeleMethodElement teleMethod) {
        for (TeleInputElement teleVar : teleMethod.getParameters()) {
            //TODO: handle compounds
            TeleParameterElement teleParam = (TeleParameterElement) teleVar;
            // param = param(openApi, operation,
            cb.add("$N = $N($N, $N, ",
                    OpenApiScheme.PARAM_VAR,
                    OpenApiScheme.CREATE_PARAM_METHOD,
                    OpenApiScheme.API_VAR,
                    OpenApiScheme.OPERATION_VAR);

            // ValueClass.class, paramName, originName, readerClass, jsonField

            ServiceCodegenUtils.generateTeleArgumentType(teleParam, cb);

            String paramName = RestletCodegenUtils.getParamName(teleParam);
            cb.add(", $S", paramName);

            String originName = RestletCodegenUtils.getOriginName(teleParam);
            cb.add(", $S", originName);

            TypeMirror customReader = RestletCodegenUtils.getCustomReaderClass(teleParam, getProcessorContext().getElementUtils());
            if (customReader != null) {
                cb.add(", $T.class", TypeName.get(customReader));
            } else {
                cb.add(", null");
            }

            JsonFieldElement jsonField = teleParam.getProperty(JsonFieldElement.class);
            if (jsonField != null) {
                cb.add(", $T.class",
                        ClassName.bestGuess(jsonField.getParentRequest().getJsonRequestClassName())
                );
            } else {
                cb.add(", null");
            }

            cb.add(");\n");
        }
    }
}
