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

package colesico.framework.restlet.codegen;


import colesico.framework.assist.CollectionUtils;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.restlet.Restlet;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.router.codegen.RoutesModulator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.service.codegen.model.TeleVarElement;
import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.codegen.TeleHttpCodegenUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
public class RestletModulator extends
        RoutesModulator<RestletTeleDriver, RestletDataPort, RestletTRContext, RestletTWContext, RestletTIContext> {

    @Override
    protected String getTeleType() {
        return Restlet.class.getSimpleName();
    }

    @Override
    protected boolean isTeleFacadeSupported(ServiceElement serviceElm) {
        AnnotationAssist teleAnn = serviceElm.getOriginClass().getAnnotation(Restlet.class);
        return teleAnn != null;
    }

    @Override
    protected Class<RestletTeleDriver> getTeleDriverClass() {
        return RestletTeleDriver.class;
    }

    @Override
    protected Class<RestletDataPort> getDataPortClass() {
        return RestletDataPort.class;
    }

    @Override
    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return CollectionUtils.annotationClassSet(Restlet.class);
    }

    @Override
    protected CodeBlock generateReadingContext(TeleParamElement teleParam) {
        String paramName = TeleHttpCodegenUtils.getParamName(teleParam);

        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("new $T(", ClassName.get(RestletTRContext.class));
        cb.add("$S", paramName);

        // Param origin
        TeleVarElement rootVar = TeleHttpCodegenUtils.getRootVar(teleParam);
        Origin paramOrigin = TeleHttpCodegenUtils.getParamOrigin(teleParam, rootVar);
        cb.add(", $T.$N", ClassName.get(RestletOriginFacade.class), paramOrigin.name());

        TypeName customReader = getCustomReaderClass(teleParam, rootVar);
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
        cb.add("new $T(", ClassName.get(RestletTWContext.class));

        TypeName writerClass = getCustomWriterClass(teleMethod);
        if (writerClass == null) {
            cb.add("null");
        } else {
            cb.add("$T.class", writerClass);
        }
        cb.add(")");
        return cb.build();
    }

    protected TypeName getCustomWriterClass(TeleMethodElement teleMethod) {
        var wrAnn = teleMethod.getProxyMethod().getOriginMethod().getAnnotation(RestletResponseWriter.class);
        if (wrAnn == null) {
            wrAnn = teleMethod.getParentTeleFacade().getParentService().getOriginClass().getAnnotation(RestletResponseWriter.class);
        }
        if (wrAnn == null) {
            return null;
        }
        TypeMirror readerClassMirror = wrAnn.getValueTypeMirror(a -> a.value());
        return TypeName.get(readerClassMirror);
    }

    protected TypeName getCustomReaderClass(TeleParamElement teleParam, TeleVarElement rootVar) {
        var rdAnn = teleParam.getOriginVariable().getAnnotation(RestletParamReader.class);
        if (rdAnn == null) {
            rdAnn = rootVar.getOriginVariable().getAnnotation(RestletParamReader.class);
        }
        if (rdAnn == null) {
            return null;
        }
        TypeMirror readerClassMirror = rdAnn.getValueTypeMirror(a -> a.value());
        return TypeName.get(readerClassMirror);
    }

}
