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
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.telehttp.Origin;
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
public class WebletModulator extends RoutesModulator {

    @Override
    protected String getTeleType() {
        return Weblet.class.getSimpleName();
    }

    @Override
    protected boolean isTeleFacadeSupported(ServiceElement serviceElm) {
        AnnotationAssist teleAnn = serviceElm.getOriginClass().getAnnotation(Weblet.class);
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
    protected CodeBlock generateReadingContext(TeleParamElement teleParam) {
        String paramName = TeleHttpCodegenUtils.getParamName(teleParam);

        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("new $T(", ClassName.get(WebletTRContext.class));
        cb.add("$S", paramName);

        // Param origin

        Origin paramOrigin = TeleHttpCodegenUtils.getParamOrigin(teleParam);
        cb.add(", $T.$N", ClassName.get(WebletOriginFacade.class), paramOrigin.name());

        TypeName customReader = getCustomReaderClass(teleParam);
        if (customReader == null) {
            cb.add(", null");
        } else {
            cb.add(", $T.class", customReader);
        }

        cb.add(")");
        return cb.build();
    }

    @Override
    protected CodeBlock generateWritingContext(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("new $T(", ClassName.get(WebletTWContext.class));

        TypeName customWriter = getCustomWriterClass(teleMethod);
        if (customWriter == null) {
            cb.add("null");
        } else {
            cb.add("$T.class", customWriter);
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

    protected TypeName getCustomReaderClass(TeleParamElement teleParam) {
        var rdAnn = teleParam.getOriginParam().getAnnotation(WebletParamReader.class);
        if (rdAnn == null) {
            return null;
        }
        TypeMirror readerClassMirror = rdAnn.getValueTypeMirror(a -> a.value());
        return TypeName.get(readerClassMirror);
    }

}
