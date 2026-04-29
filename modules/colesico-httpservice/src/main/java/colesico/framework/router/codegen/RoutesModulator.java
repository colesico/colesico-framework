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
package colesico.framework.router.codegen;

import colesico.framework.assist.codegen.ArrayCodegen;
import colesico.framework.router.RouterCommands;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.teleapi.TeleFacadeElement;
import colesico.framework.service.codegen.model.teleapi.TeleCommandElement;
import colesico.framework.service.codegen.modulator.TeleFacadeModulator;
import colesico.framework.teleapi.dataport.TRContext;
import colesico.framework.teleapi.dataport.TWContext;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Routes modulation support
 */
abstract public class RoutesModulator extends TeleFacadeModulator<RouterTeleFacadeElement> {

    protected final Logger logger = LoggerFactory.getLogger(RoutesModulator.class);

    abstract protected Class<?> commandsClass();

    abstract protected Class<? extends TRContext> readContextClass();

    abstract protected Class<? extends TWContext> writeContextClass();

    @Override
    protected void processTeleCommand(TeleCommandElement teleCommandElement) {
        ((RouterTeleFacadeElement) teleCommandElement.parentTeleFacade())
                .routesBuilder()
                .addTeleCommand(teleCommandElement);
    }

    @Override
    protected RouterTeleFacadeElement createTeleFacade(ServiceElement serviceElm) {
        return new RouterTeleFacadeElement(
                teleType(),
                commandsClass(),
                readContextClass(),
                writeContextClass(),
                TeleFacadeElement.IocQualifier.ofEmpty(),
                new RoutesBuilder(serviceElm)
        );
    }

    protected CodeBlock generatecommandsMethodBody(RouterTeleFacadeElement teleFacade) {
        CodeBlock.Builder cb = CodeBlock.builder();

        cb.addStatement("$T $N = new $T($T.class)",
                ClassName.get(RouterCommands.class),
                COMMANDS_VAR,
                ClassName.get(RouterCommands.class),
                TypeName.get(teleFacade.parentService().originClass().originType())
        );

        RoutesBuilder routesBuilder = teleFacade.routesBuilder();

        for (RoutesBuilder.RoutedTeleCommandElement routedTeleCommand : routesBuilder.teleCommands()) {
            cb.add(generateRouteMapping(teleFacade, routedTeleCommand));
        }

        cb.addStatement("return $N", COMMANDS_VAR);
        return cb.build();
    }

    protected CodeBlock generateRouteMapping(TeleFacadeElement teleFacade, RoutesBuilder.RoutedTeleCommandElement routedTeleCommand) {

        CodeBlock.Builder cb = CodeBlock.builder();

        // commands.add("GET/foo/bla",this:commandMethod,"targetMethodName")
        cb.add("$N.$N($S, this::$N, $S, ",
                COMMANDS_VAR,
                RouterCommands.ADD_METHOD,
                routedTeleCommand.route(),
                routedTeleCommand.teleCommand().commandMethodName(),
                routedTeleCommand.teleCommand().targetMethodName()
        );

        if (routedTeleCommand.routeAttributes().isEmpty()) {
            cb.add("null");
        } else {
            ArrayCodegen attrCodegen = new ArrayCodegen();
            for (Map.Entry<String, String> param : routedTeleCommand.routeAttributes().entrySet()) {
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
