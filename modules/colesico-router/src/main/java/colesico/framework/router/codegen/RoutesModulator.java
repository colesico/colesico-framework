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

import colesico.framework.router.Router;
import colesico.framework.router.RoutingLigature;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.modulator.TeleModulator;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Vladlen Larionov
 */
abstract public class RoutesModulator<D extends TeleDriver<R, W, I, P>, P extends DataPort<R, W>, R, W, I>
        extends TeleModulator<D, P, R, W, I, RoutegenContext, RoutingLigature, Router> {

    protected static final String ROUTES_MAPPER_CLASS_SUFFIX = "Routes";
    protected static final String LIGATURE_VAR = "ligature";

    protected final Logger logger = LoggerFactory.getLogger(RoutesModulator.class);

    @Override
    protected Class<RoutegenContext> getTeleModulatorContextClass() {
        return RoutegenContext.class;
    }

    @Override
    protected RoutegenContext createTeleModulatorContext(ServiceElement serviceElm) {
        return new RoutegenContext(serviceElm) {
        };
    }

    @Override
    protected Class<RoutingLigature> getLigatureClass() {
        return RoutingLigature.class;
    }

    @Override
    protected Class<Router> getQualifierClass() {
        return Router.class;
    }

    protected CodeBlock generateLigatureMethodBody(TeleFacadeElement teleFacade) {
        CodeBlock.Builder cb = CodeBlock.builder();

        cb.addStatement("$T $N = new $T($T.class)",
                ClassName.get(RoutingLigature.class),
                LIGATURE_VAR,
                ClassName.get(RoutingLigature.class),
                TypeName.get(teleFacade.getParentService().getOriginClass().asType())
        );

        RoutegenContext routegenContext = teleFacade.getProperty(RoutegenContext.class);

        for (RoutegenContext.RoutedTeleMethodElement routedTeleMethod : routegenContext.getTeleMethods()) {
            cb.add(generateRouteMapping(teleFacade, routedTeleMethod));
        }

        cb.addStatement("return $N", LIGATURE_VAR);
        return cb.build();
    }

    protected CodeBlock generateRouteMapping(TeleFacadeElement teleFacade, RoutegenContext.RoutedTeleMethodElement routedTeleMethod) {

        CodeBlock.Builder cb = CodeBlock.builder();
        cb.addStatement("$N.$N($S,this::$N,$S)",
                LIGATURE_VAR,
                RoutingLigature.ADD_METHOD,
                routedTeleMethod.getRoute(),
                routedTeleMethod.getTeleMethod().getName(),
                routedTeleMethod.getTeleMethod().getName()
        );

        return cb.build();
    }
}
