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
import colesico.framework.telehttp.codegen.HttpTeleReadElement;
import colesico.framework.telehttp.codegen.HttpTeleWriteElement;
import colesico.framework.telehttp.codegen.TeleHttpCodegenUtils;
import colesico.framework.weblet.Weblet;
import colesico.framework.weblet.teleapi.*;
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
    protected TeleReadElement createReadValue(TeleOrdinaryParamElement teleParam) {

        CodeBlock.Builder valueTypeCode = CodeBlock.builder();
        ServiceCodegenUtils.generateTeleParamType(teleParam, valueTypeCode);

        String paramName = TeleHttpCodegenUtils.paramName(teleParam);

        CodeBlock.Builder optionsCode = CodeBlock.builder();
        optionsCode.add("$T.$N(", ClassName.get(WebletReadOptions.class), WebletReadOptions.OF_METHOD);

        String originName = TeleHttpCodegenUtils.originName(teleParam, WebletOrigin.AUTO);

        TypeMirror customReader = getCustomReaderClass(teleParam);

        optionsCode.add("$S", paramName);

        if (!originName.equals(WebletOrigin.AUTO) || customReader != null) {
            optionsCode.add(", $S", originName);
        }

        ClassType customReaderCT = null;

        if (customReader != null) {
            optionsCode.add(", $T.class", TypeName.get(customReader));
            customReaderCT = new ClassType(processorContext().processingEnv(), (DeclaredType) customReader);
        }

        optionsCode.add(")");

        return new HttpTeleReadElement(teleParam,
                valueTypeCode.build(),
                optionsCode.build(),
                paramName,
                originName,
                customReaderCT
        );
    }

    @Override
    protected TeleWriteElement createWriteResult(TeleCommandElement teleCommand) {
        CodeBlock.Builder valueTypeCode = CodeBlock.builder();
        ServiceCodegenUtils.generateTeleResultType(teleCommand, valueTypeCode);

        CodeBlock.Builder optionsCode = CodeBlock.builder();
        optionsCode.add("$T.$N(", ClassName.get(WebletWriteOptions.class), WebletWriteOptions.OF_METHOD);
        TypeMirror customWriter = getCustomWriterClass(teleCommand);
        ClassType customWriterCT = null;
        if (customWriter != null) {
            optionsCode.add("$T.class", TypeName.get(customWriter));
            customWriterCT = new ClassType(processorContext().processingEnv(), (DeclaredType) customWriter);
        }
        optionsCode.add(")");

        return new HttpTeleWriteElement(teleCommand, valueTypeCode.build(), optionsCode.build(), customWriterCT);
    }

    private TypeMirror getCustomWriterClass(TeleCommandElement teleCommand) {
        var wrAnn = teleCommand.serviceMethod().originMethod().annotation(WebletResponseWriter.class);
        if (wrAnn == null) {
            wrAnn = teleCommand.parentTeleService().parentService().originClass().annotation(WebletResponseWriter.class);
        }
        if (wrAnn == null) {
            return null;
        }
        return wrAnn.valueTypeMirror(a -> a.value());
    }

    private TypeMirror getCustomReaderClass(TeleOrdinaryParamElement teleParam) {
        var rdAnn = teleParam.originElement().annotation(WebletParamReader.class);

        if (rdAnn == null) {
            rdAnn = teleParam.parentTeleCommand().serviceMethod().originMethod().annotation(WebletParamReader.class);
        }

        if (rdAnn != null) {
            return rdAnn.valueTypeMirror(a -> a.value());
        }

        return null;
    }

}
