/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.weblet.codegen;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.router.RouterCommandsRegistry;
import colesico.framework.router.codegen.RoutesModulator;
import colesico.framework.service.codegen.assist.ServiceCodegenUtils;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.model.teleapi.TeleReadElement;
import colesico.framework.service.codegen.model.teleapi.TeleWriteElement;
import colesico.framework.service.codegen.model.teleapi.TeleCommandElement;
import colesico.framework.service.codegen.model.teleapi.TeleOrdinaryParamElement;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;
import colesico.framework.telehttp.UseReader;
import colesico.framework.telehttp.UseWriter;
import colesico.framework.telehttp.codegen.TeleHttpReadElement;
import colesico.framework.telehttp.codegen.TeleHttpWriteElement;
import colesico.framework.telehttp.codegen.TeleHttpCodegenUtils;
import colesico.framework.telehttp.origin.Origin;
import colesico.framework.weblet.*;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
public final class WebletModulator extends RoutesModulator {

    @Override
    protected Class<?> teleType() {
        return Weblet.class;
    }

    @Override
    protected boolean isTeleServiceSupported(ServiceElement service) {
        var teleAnn = service.originClass().annotation(Weblet.class);
        return teleAnn != null;
    }

    @Override
    protected Class<? extends TeleFacade.CommandsRegistry> commandsClass() {
        return RouterCommandsRegistry.class;
    }

    @Override
    protected Class<? extends ReadOptions> readOptionsClass() {
        return WebletReadOptions.class;
    }

    @Override
    protected Class<? extends WriteOptions> writeOptionsClass() {
        return WebletWriteOptions.class;
    }

    @Override
    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return Set.of(Weblet.class);
    }

    @Override
    protected TeleReadElement createTeleRead(TeleOrdinaryParamElement teleParam) {

        CodeBlock.Builder valueTypeCode = CodeBlock.builder();
        ServiceCodegenUtils.generateTeleParamType(teleParam, valueTypeCode);


        CodeBlock.Builder optionsCode = CodeBlock.builder();
        optionsCode.add("$T.$N(", ClassName.get(WebletReadOptions.class), WebletReadOptions.BUILDER_METHOD);
        optionsCode.add(valueTypeCode.build());
        optionsCode.add(")");

        String paramName = TeleHttpCodegenUtils.paramName(teleParam);
        if (paramName != null) {
            optionsCode.add(".$N($S)", WebletReadOptions.PARAM_NAME_METHOD, paramName);
        }

        String originName = TeleHttpCodegenUtils.originName(teleParam, Origin.AUTO);
        if (originName != null) {
            optionsCode.add(".$N($S)", WebletReadOptions.ORIGIN_NAME_METHOD, originName);
        }

        TypeMirror customReader = TeleHttpCodegenUtils.customReaderClass(teleParam);
        ClassType customReaderCT = null;
        if (customReader != null) {
            optionsCode.add(".$N($T.class)", WebletReadOptions.CUSTOM_READER_METHOD, TypeName.get(customReader));
            customReaderCT = new ClassType(processorContext().processingEnv(), (DeclaredType) customReader);
        }

        optionsCode.add(".$N()", WebletWriteOptions.BUILD_METHOD);

        return new TeleHttpReadElement(teleParam,
                null,
                optionsCode.build(),
                paramName,
                originName,
                customReaderCT
        );
    }

    @Override
    protected TeleWriteElement createTeleWrite(TeleCommandElement teleCommand) {
        CodeBlock.Builder valueTypeCode = CodeBlock.builder();
        ServiceCodegenUtils.generateTeleResultType(teleCommand, valueTypeCode);

        CodeBlock.Builder optionsCode = CodeBlock.builder();
        optionsCode.add("$T.$N(", ClassName.get(WebletWriteOptions.class), WebletWriteOptions.BUILDER_METHOD);
        optionsCode.add(valueTypeCode.build());
        optionsCode.add(")");

        TypeMirror customWriter = getCustomWriterClass(teleCommand);
        ClassType customWriterCT = null;
        if (customWriter != null) {
            // .customWriter(CustomWriter.class)
            optionsCode.add(".$N($T.class)", WebletWriteOptions.CUSTOM_WRITER_METHOD, TypeName.get(customWriter));
            customWriterCT = new ClassType(processorContext().processingEnv(), (DeclaredType) customWriter);
        }
        // .build()
        optionsCode.add(".$N()", WebletWriteOptions.BUILD_METHOD);


        return new TeleHttpWriteElement(teleCommand, null, optionsCode.build(), customWriterCT);
    }

    private TypeMirror getCustomWriterClass(TeleCommandElement teleCommand) {
        var wrAnn = teleCommand.serviceMethod().originMethod().annotation(UseWriter.class);
        if (wrAnn == null) {
            wrAnn = teleCommand.parentTeleService().parentService().originClass().annotation(UseWriter.class);
        }
        if (wrAnn == null) {
            return null;
        }
        return wrAnn.valueTypeMirror(a -> a.value());
    }



}
