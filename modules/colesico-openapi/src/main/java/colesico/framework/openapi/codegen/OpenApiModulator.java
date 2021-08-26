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


import colesico.framework.openapi.OpenApiBuilder;
import colesico.framework.restlet.Restlet;
import colesico.framework.restlet.codegen.RestletModulator;
import colesico.framework.router.codegen.RouterTeleFacadeElement;
import colesico.framework.router.codegen.RoutesBuilder;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.telescheme.TeleSchemeBuilder;
import colesico.framework.telescheme.codegen.modulator.TeleSchemeModulator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;

/**
 * Modulates Swagger definition
 */
public final class OpenApiModulator extends TeleSchemeModulator {

    public static final String OPENAPI_VAR = "openApi";
    public static final String OPERATION_VAR = "operation";

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
        cb.addStatement("$T $N = $N()", ClassName.get(OpenAPI.class), OPENAPI_VAR, OpenApiBuilder.CREATE_OPEN_API_METHOD);

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
        RouterTeleFacadeElement routedTeleFacade = (RouterTeleFacadeElement) teleFacade;
        RoutesBuilder routesBuilder = routedTeleFacade.getRoutesBuilder();

        for (TeleMethodElement teleMethod : teleFacade.getTeleMethods()) {
            RoutesBuilder.RoutedTeleMethodElement routedTeleMethod = teleMethod.getProperty(RoutesBuilder.RoutedTeleMethodElement.class);
            if (routedTeleMethod == null){
                continue;
            }

            cb.addStatement("$N = $N($S)",
                    OPERATION_VAR,
                    OpenApiBuilder.CREATE_OPERATION_METHOD,
                    teleMethod.getServiceMethod().getName()
            );

            String path = routedTeleMethod.getRoutePath();
            String httpMethod = routedTeleMethod.getHttpMethodName();
            cb.addStatement("$N($N,$S,$S,$N)",
                    OpenApiBuilder.ADD_OPERATION_METHOD,
                    OPENAPI_VAR,
                    path,
                    httpMethod,
                    OPERATION_VAR
            );
        }
    }
}
