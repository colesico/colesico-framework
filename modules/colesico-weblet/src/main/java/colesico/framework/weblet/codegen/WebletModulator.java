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
import colesico.framework.router.codegen.RoutesModulator;
import colesico.framework.service.codegen.assist.ServiceCodegenUtils;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.model.teleapi.TRContextElement;
import colesico.framework.service.codegen.model.teleapi.TWContextElement;
import colesico.framework.service.codegen.model.teleapi.TeleCommandElement;
import colesico.framework.service.codegen.model.teleapi.TeleParameterElement;
import colesico.framework.teleapi.dataport.TRContext;
import colesico.framework.teleapi.dataport.TWContext;
import colesico.framework.telehttp.codegen.HttpTRContextElement;
import colesico.framework.telehttp.codegen.HttpTWContextElement;
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
    protected boolean isTeleFacadeSupported(ServiceElement service) {
        var teleAnn = service.originClass().annotation(Weblet.class);
        return teleAnn != null;
    }

    @Override
    protected Class<WebletTeleController> commandsClass() {
        return WebletTeleController.class;
    }

    @Override
    protected Class<? extends TRContext> readContextClass() {
        return WebletTRContext.class;
    }

    @Override
    protected Class<? extends TWContext> writeContextClass() {
        return WebletTWContext.class;
    }

    @Override
    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return Set.of(Weblet.class);
    }

    @Override
    protected TRContextElement createReadContext(TeleParameterElement teleParam) {

        String paramName = TeleHttpCodegenUtils.paramName(teleParam);

        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("$T.$N(", ClassName.get(WebletTRContext.class), WebletTRContext.OF_METHOD);

        ServiceCodegenUtils.generateTeleInputType(teleParam, cb);
        cb.add(",");

        String originName = TeleHttpCodegenUtils.originName(teleParam, WebletOrigin.AUTO);

        TypeMirror customReader = getCustomReaderClass(teleParam);

        cb.add("$S", paramName);

        if (!originName.equals(WebletOrigin.AUTO) || customReader != null) {
            cb.add(", $S", originName);
        }

        ClassType customReaderCT = null;

        if (customReader != null) {
            cb.add(", $T.class", TypeName.get(customReader));
            customReaderCT = new ClassType(processorContext().processingEnv(), (DeclaredType) customReader);
        }

        cb.add(")");

        return new HttpTRContextElement(teleParam,
                cb.build(),
                paramName,
                originName,
                customReaderCT
        );
    }

    @Override
    protected TWContextElement createWriteContext(TeleCommandElement teleCommand) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("$T.$N(", ClassName.get(WebletTWContext.class), WebletTWContext.OF_METHOD);

        ServiceCodegenUtils.generateTeleResultType(teleCommand, cb);

        TypeMirror customWriter = getCustomWriterClass(teleCommand);
        ClassType customWriterCT = null;
        if (customWriter != null) {
            cb.add(", $T.class", TypeName.get(customWriter));
            customWriterCT = new ClassType(processorContext().processingEnv(), (DeclaredType) customWriter);
        }
        cb.add(")");
        return new HttpTWContextElement(teleCommand, cb.build(), customWriterCT);
    }

    private TypeMirror getCustomWriterClass(TeleCommandElement teleCommand) {
        var wrAnn = teleCommand.serviceMethod().originMethod().annotation(WebletResponseWriter.class);
        if (wrAnn == null) {
            wrAnn = teleCommand.parentTeleFacade().parentService().originClass().annotation(WebletResponseWriter.class);
        }
        if (wrAnn == null) {
            return null;
        }
        return wrAnn.valueTypeMirror(a -> a.value());
    }

    private TypeMirror getCustomReaderClass(TeleParameterElement teleParam) {
        var rdAnn = teleParam.originElement().annotation(WebletParamReader.class);

        if (rdAnn == null) {
            rdAnn = teleParam.parentTeleCommand().serviceMethod().originMethod().annotation(WebletParamReader.class);
        }
        return rdAnn.valueTypeMirror(a -> a.value());
    }

}
