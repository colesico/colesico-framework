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

import colesico.framework.assist.codegen.model.AnnotationToolbox;
import colesico.framework.assist.codegen.model.VarElement;
import colesico.framework.router.Router;
import colesico.framework.router.RoutingLigature;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Deque;


/**
 * @author Vladlen Larionov
 */
abstract public class RoutesModulator extends Modulator {

    protected static final String ROUTES_MAPPER_CLASS_SUFFIX = "Routes";
    protected static final String LIGATURE_VAR = "ligature";

    protected final Logger logger = LoggerFactory.getLogger(RoutesModulator.class);

    abstract protected Class<? extends Annotation> getTeleAnnotation();

    abstract protected String getTeleType();

    abstract protected Class<? extends TeleDriver> getTeleDriverClass();

    abstract protected Class<? extends DataPort> getDataPortClass();

    protected Class<?> getInvocationOptionsClass() {
        return Object.class;
    }

    protected Class<?> getLigatureClass() {
        return RoutingLigature.class;
    }

    protected CodeBlock generateInvokingContext(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("null");
        return cb.build();
    }

    protected CodeBlock generateWritingContext(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("null");
        return cb.build();
    }

    protected CodeBlock generateReadingContext(TeleParamElement teleParam) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("null");
        return cb.build();
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

    @Override
    public void onAddTeleFacade(ServiceElement serviceElm) {
        super.onService(serviceElm);
        AnnotationToolbox teleAnn = serviceElm.getOriginClass().getAnnotation(getTeleAnnotation());

        if (teleAnn == null) {
            return;
        }

        TeleFacadeElement teleFacade = new TeleFacadeElement(
                getTeleType(),
                getTeleDriverClass(),
                getDataPortClass(),
                getLigatureClass(),
                TeleFacadeElement.IocQualifiers.ofClassed(Router.class));
        serviceElm.addTeleFacade(teleFacade);

        RoutegenContext routegenContext = new RoutegenContext(serviceElm) {
        };
        teleFacade.setProperty(RoutegenContext.class, routegenContext);
    }

    @Override
    public void onAddTeleMethod(TeleMethodElement teleMethod) {
        super.onAddTeleMethod(teleMethod);
        TeleFacadeElement teleFacade = teleMethod.getParentTeleFacade();
        if (!teleFacade.getTeleType().equals(getTeleType())) {
            return;
        }
        RoutegenContext routegenContext = teleFacade.getProperty(RoutegenContext.class);
        routegenContext.registTeleMethod(teleMethod);
        teleMethod.setInvokingContext(generateInvokingContext(teleMethod));
        teleMethod.setWritingContext(generateWritingContext(teleMethod));
    }

    @Override
    public void onLinkTeleParam(TeleParamElement teleParam, Deque<VarElement> varStack) {
        super.onLinkTeleParam(teleParam, varStack);
        TeleMethodElement teleMethod = teleParam.getParentTeleMethod();
        TeleFacadeElement teleFacade = teleMethod.getParentTeleFacade();
        if (!teleFacade.getTeleType().equals(getTeleType())) {
            return;
        }
        teleParam.setReadingContext(generateReadingContext(teleParam));
    }

    @Override
    public void onTeleFacadeParsed(TeleFacadeElement teleFacade) {
        super.onTeleFacadeParsed(teleFacade);
        if (!teleFacade.getTeleType().equals(getTeleType())) {
            return;
        }
        teleFacade.setLigatureMethodBody(generateLigatureMethodBody(teleFacade));
    }

}
