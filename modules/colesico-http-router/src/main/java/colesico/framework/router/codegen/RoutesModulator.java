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
import colesico.framework.router.RouterCommandsRegistry;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.teleapi.TeleServiceElement;
import colesico.framework.service.codegen.model.teleapi.TeleCommandElement;
import colesico.framework.service.codegen.modulator.TeleServiceModulator;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Routes modulation support
 */
abstract public class RoutesModulator extends TeleServiceModulator<RouterTeleServiceElement> {

    protected final Logger logger = LoggerFactory.getLogger(RoutesModulator.class);

    abstract protected Class<? extends ReadOptions> readOptionsClass();

    abstract protected Class<? extends WriteOptions> writeOptionsClass();

    protected Class<? extends TeleFacade.CommandsRegistry> commandsClass(){
        return RouterCommandsRegistry.class;
    }

    @Override
    protected void processTeleCommand(TeleCommandElement teleCommandElement) {
        ((RouterTeleServiceElement) teleCommandElement.parentTeleService())
                .routesBuilder()
                .addTeleCommand(teleCommandElement);
    }

    @Override
    protected RouterTeleServiceElement createTeleService(ServiceElement serviceElm) {
        return new RouterTeleServiceElement(
                teleType(),
                commandsClass(),
                readOptionsClass(),
                writeOptionsClass(),
                TeleServiceElement.IocQualifier.ofEmpty(),
                new RoutesBuilder(serviceElm)
        );
    }

    protected CodeBlock generateCommandsMethodBody(RouterTeleServiceElement teleService) {
        CodeBlock.Builder cb = CodeBlock.builder();

        cb.addStatement("$T $N = new $T($T.class)",
                ClassName.get(RouterCommandsRegistry.class),
                COMMANDS_VAR,
                ClassName.get(RouterCommandsRegistry.class),
                TypeName.get(teleService.parentService().originClass().originType())
        );

        RoutesBuilder routesBuilder = teleService.routesBuilder();

        for (RoutesBuilder.RoutedTeleCommandElement routedTeleCommand : routesBuilder.teleCommands()) {
            cb.add(generateRouteMapping(teleService, routedTeleCommand));
        }

        cb.addStatement("return $N", COMMANDS_VAR);
        return cb.build();
    }

    protected CodeBlock generateRouteMapping(TeleServiceElement teleFacade, RoutesBuilder.RoutedTeleCommandElement routedTeleCommand) {

        CodeBlock.Builder cb = CodeBlock.builder();

        // commands.add("GET/foo/bla",this:commandMethod,"targetMethodName")
        cb.add("$N.$N($S, this::$N, $S, ",
                COMMANDS_VAR,
                RouterCommandsRegistry.ADD_METHOD,
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
