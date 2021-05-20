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
package colesico.framework.router.codegen;

import colesico.framework.assist.codegen.ArrayCodegen;
import colesico.framework.router.Router;
import colesico.framework.router.RoutingLigature;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.modulator.TeleModulator;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Routes modulation support
 */
abstract public class RoutesModulator extends TeleModulator<RouterTeleFacadeElement> {

    protected final Logger logger = LoggerFactory.getLogger(RoutesModulator.class);

    abstract protected Class<? extends TeleDriver> getTeleDriverClass();

    abstract protected Class<? extends DataPort> getDataPortClass();

    @Override
    protected void processTeleMethod(TeleMethodElement teleMethodElement) {
        ((RouterTeleFacadeElement) teleMethodElement.getParentTeleFacade())
                .getRoutesBuilder()
                .addTeleMethod(teleMethodElement);
    }

    @Override
    protected final RouterTeleFacadeElement createTeleFacade(ServiceElement serviceElm) {
        return new RouterTeleFacadeElement(
                getTeleType(),
                getTeleDriverClass(),
                getDataPortClass(),
                RoutingLigature.class,
                TeleFacadeElement.IocQualifier.ofClassed(Router.class),
                new RoutesBuilder(serviceElm)
        );
    }

    protected final CodeBlock generateLigatureMethodBody(RouterTeleFacadeElement teleFacade) {
        CodeBlock.Builder cb = CodeBlock.builder();

        cb.addStatement("$T $N = new $T($T.class)",
                ClassName.get(RoutingLigature.class),
                LIGATURE_VAR,
                ClassName.get(RoutingLigature.class),
                TypeName.get(teleFacade.getParentService().getOriginClass().getOriginType())
        );

        RoutesBuilder routesBuilder = teleFacade.getRoutesBuilder();

        for (RoutesBuilder.RoutedTeleMethodElement routedTeleMethod : routesBuilder.getTeleMethods()) {
            cb.add(generateRouteMapping(teleFacade, routedTeleMethod));
        }

        cb.addStatement("return $N", LIGATURE_VAR);
        return cb.build();
    }

    protected final CodeBlock generateRouteMapping(TeleFacadeElement teleFacade, RoutesBuilder.RoutedTeleMethodElement routedTeleMethod) {

        CodeBlock.Builder cb = CodeBlock.builder();

        cb.add("$N.$N($S,$N(),$S,",
                LIGATURE_VAR,
                RoutingLigature.ADD_METHOD,
                routedTeleMethod.getRoute(),
                routedTeleMethod.getTeleMethod().getBuilderName(),
                routedTeleMethod.getTeleMethod().getName()
        );

        if (routedTeleMethod.getRouteAttributes().isEmpty()) {
            cb.add("null");
        } else {
            ArrayCodegen attrCodegen = new ArrayCodegen();
            for (Map.Entry<String, String> param : routedTeleMethod.getRouteAttributes().entrySet()) {
                attrCodegen.add("$S", param.getKey());
                attrCodegen.add("$S", param.getValue());
            }
            // Map.of("attrName","attrValue"...)
            cb.add("$T.of(", ClassName.get(Map.class));
            cb.add(attrCodegen.toFormat(), attrCodegen.toValues());
            cb.add(")");
        }

        cb.add(");\n");
        return cb.build();
    }
}
