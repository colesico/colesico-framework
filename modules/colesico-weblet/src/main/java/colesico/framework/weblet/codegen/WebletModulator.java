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

package colesico.framework.weblet.codegen;

import colesico.framework.assist.CollectionUtils;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.router.codegen.RoutesModulator;
import colesico.framework.service.codegen.assist.ServiceCodegenUtils;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParameterElement;
import colesico.framework.telehttp.codegen.TeleHttpCodegenUtils;
import colesico.framework.weblet.Weblet;
import colesico.framework.weblet.teleapi.*;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
public final class WebletModulator extends RoutesModulator {

    @Override
    protected Class<?> getTeleType() {
        return Weblet.class;
    }

    @Override
    protected boolean isTeleFacadeSupported(ServiceElement service) {
        AnnotationAssist teleAnn = service.getOriginClass().getAnnotation(Weblet.class);
        return teleAnn != null;
    }

    @Override
    protected Class<WebletTeleDriver> getTeleDriverClass() {
        return WebletTeleDriver.class;
    }

    @Override
    protected Class<WebletDataPort> getDataPortClass() {
        return WebletDataPort.class;
    }

    @Override
    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return CollectionUtils.annotationClassSet(Weblet.class);
    }

    @Override
    protected CodeBlock generateReadingContext(TeleParameterElement teleParam) {

        String paramName = TeleHttpCodegenUtils.getParamName(teleParam);

        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("$T.$N(", ClassName.get(WebletTRContext.class), WebletTRContext.OF_METHOD);

        ServiceCodegenUtils.generateTeleArgumentType(teleParam, cb);
        cb.add(",");

        String originName = TeleHttpCodegenUtils.getOriginName(teleParam, WebletOrigin.AUTO);

        TypeName customReader = getCustomReaderClass(teleParam);

        cb.add("$S", paramName);

        if (!originName.equals(WebletOrigin.AUTO) || customReader != null) {
            cb.add(", $S", originName);
        }

        if (customReader != null) {
            cb.add(", $T.class", customReader);
        }

        cb.add(")");
        return cb.build();
    }

    @Override
    protected CodeBlock generateWritingContext(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("$T.$N(", ClassName.get(WebletTWContext.class), WebletTWContext.OF_METHOD);

        ServiceCodegenUtils.generateTeleResultType(teleMethod, cb);

        TypeName customWriter = getCustomWriterClass(teleMethod);
        if (customWriter != null) {
            cb.add(", $T.class", customWriter);
        }
        cb.add(")");
        return cb.build();
    }

    protected TypeName getCustomWriterClass(TeleMethodElement teleMethod) {
        var wrAnn = teleMethod.getServiceMethod().getOriginMethod().getAnnotation(WebletResponseWriter.class);
        if (wrAnn == null) {
            wrAnn = teleMethod.getParentTeleFacade().getParentService().getOriginClass().getAnnotation(WebletResponseWriter.class);
        }
        if (wrAnn == null) {
            return null;
        }
        TypeMirror readerClassMirror = wrAnn.getValueTypeMirror(a -> a.value());
        return TypeName.get(readerClassMirror);
    }

    protected TypeName getCustomReaderClass(TeleParameterElement teleParam) {
        var rdAnn = teleParam.getOriginElement().getAnnotation(WebletParamReader.class);
        if (rdAnn == null) {
            return null;
        }
        if (rdAnn == null) {
            rdAnn = teleParam.getParentTeleMethod().getServiceMethod().getOriginMethod().getAnnotation(WebletParamReader.class);
        }
        TypeMirror readerClassMirror = rdAnn.getValueTypeMirror(a -> a.value());
        return TypeName.get(readerClassMirror);
    }

}
