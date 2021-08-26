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


import colesico.framework.restlet.Restlet;
import colesico.framework.restlet.codegen.RestletModulator;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.telescheme.codegen.modulator.TeleSchemeModulator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;

/**
 * Modulates Swagger definition
 */
public final class OpenApiModulatorTele extends TeleSchemeModulator {

    public static final String FACADE_SCHEME_VAR="facadeScheme";

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
    protected Class<?> getTeleFacadeSchemeType() {
        return OpenAPI.class;
    }

    @Override
    protected Class<?> getTeleMethodSchemeType() {
        return Operation.class;
    }

    @Override
    protected CodeBlock generateTeleFacadeScheme(TeleFacadeElement teleFacade) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.addStatement("$T $N = new $T()", ClassName.get(OpenAPI.class),FACADE_SCHEME_VAR,ClassName.get(OpenAPI.class));
        cb.addStatement("return $N",FACADE_SCHEME_VAR);
        return cb.build();
    }

    @Override
    protected CodeBlock generateTeleMethodScheme(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        return cb.build();
    }
}
