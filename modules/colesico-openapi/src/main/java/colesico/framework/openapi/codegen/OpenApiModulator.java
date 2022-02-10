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
import colesico.framework.openapi.OpenApiBuilder;
import colesico.framework.restlet.Restlet;
import colesico.framework.restlet.codegen.RestletModulator;
import colesico.framework.restlet.codegen.assist.RestletCodegenUtils;
import colesico.framework.restlet.codegen.model.JsonFieldElement;
import colesico.framework.router.codegen.RoutesBuilder;
import colesico.framework.service.codegen.assist.ServiceCodegenUtils;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParameterElement;
import colesico.framework.service.codegen.model.TeleVarElement;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.telescheme.TeleSchemeBuilder;
import colesico.framework.telescheme.codegen.modulator.TeleSchemeModulator;
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

    public static final String OPENAPI_VAR = "openApi";
    public static final String OPERATION_VAR = "operation";
    public static final String INPUT_PARAM_VAR = "inputParam";

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
    protected Class<? extends TeleSchemeBuilder> getBuilderBaseClass() {
        return OpenApiBuilder.class;
    }

    @Override
    protected Class<?> getTeleSchemeType() {
        return OpenAPI.class;
    }

    @Override
    protected Class<?> getOperationSchemeType() {
        return Operation.class;
    }

    @Override
    protected CodeBlock generateScheme(TeleFacadeElement teleFacade) {
        CodeBlock.Builder cb = CodeBlock.builder();

        // OpenApi openApi = new OpenAPI(ServiceClassSimpleName);
        cb.addStatement("$T $N = $N($S)",
                ClassName.get(OpenAPI.class),
                OPENAPI_VAR,
                OpenApiBuilder.CREATE_OPEN_API_METHOD,
                teleFacade.getParentService().getOriginClass().getSimpleName()
        );

        generateOperations(cb, teleFacade);

        cb.addStatement("return $N", OPENAPI_VAR);
        return cb.build();
    }

    @Override
    protected CodeBlock generateOperationScheme(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        return cb.build();
    }

    protected void generateOperations(CodeBlock.Builder cb, TeleFacadeElement teleFacade) {
        cb.addStatement("$T $N", ClassName.get(Operation.class), OPERATION_VAR);
        cb.addStatement("$T $N", ClassName.get(InputParam.class), INPUT_PARAM_VAR);
        // RouterTeleFacadeElement routedTeleFacade = (RouterTeleFacadeElement) teleFacade;
        // RoutesBuilder routesBuilder = routedTeleFacade.getRoutesBuilder();

        for (TeleMethodElement teleMethod : teleFacade.getTeleMethods()) {
            RoutesBuilder.RoutedTeleMethodElement routedTeleMethod = teleMethod.getProperty(RoutesBuilder.RoutedTeleMethodElement.class);
            if (routedTeleMethod == null) {
                continue;
            }

            String path = routedTeleMethod.getRoutePath();
            String httpMethod = routedTeleMethod.getHttpMethodName();

            // operation = createOperation(openApi, methodName, path, httpMethod)
            cb.addStatement("$N = $N($N,$S,$S,$S)",
                    OPERATION_VAR,
                    OpenApiBuilder.CREATE_OPERATION_METHOD,
                    OPENAPI_VAR,
                    teleMethod.getServiceMethod().getName(),
                    path,
                    httpMethod
            );

            generateInputParams(cb, teleMethod);
        }
    }

    protected void generateInputParams(CodeBlock.Builder cb, TeleMethodElement teleMethod) {
        for (TeleVarElement teleVar : teleMethod.getParameters()) {
            //TODO: handle compounds
            TeleParameterElement teleParam = (TeleParameterElement) teleVar;
            // inputParam = createInputParam(openApi, operation,
            cb.add("$N = $N($N, $N, ",
                    INPUT_PARAM_VAR,
                    OpenApiBuilder.CREATE_INPUT_PARAM_METHOD,
                    OPENAPI_VAR,
                    OPERATION_VAR);

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
