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
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.restlet.Restlet;
import colesico.framework.restlet.codegen.model.JsonFieldElement;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.restlet.teleapi.jsonrequest.JsonField;
import colesico.framework.restlet.teleapi.reader.JsonFieldReader;
import colesico.framework.router.codegen.RoutesModulator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParamElement;
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
public final class RestletModulator extends RoutesModulator {

    @Override
    protected Class<?> getTeleType() {
        return Restlet.class;
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

    /**
     * Generate parameters wrapper class
     *
     * @param teleMethodElement
     */
    @Override
    protected void processTeleMethod(TeleMethodElement teleMethodElement) {
        super.processTeleMethod(teleMethodElement);

        //TypeSpec.Builder requestBuilder = TypeSpec.classBuilder(method.getRequestClassSimpleName());

    }

    @Override
    protected CodeBlock generateReadingContext(TeleParamElement teleParam) {
        String paramName = TeleHttpCodegenUtils.getParamName(teleParam);

        CodeBlock.Builder cb = CodeBlock.builder();

        // new RestletTRContext(paramName
        cb.add("$T.$N(", ClassName.get(RestletTRContext.class), RestletTRContext.OF_METHOD);

        String originName = TeleHttpCodegenUtils.getOriginName(teleParam);
        TypeName customReader = getCustomReaderClass(teleParam);
        JsonFieldElement jsonField = teleParam.getProperty(JsonFieldElement.class);

        cb.add("$S", paramName);

        if (!originName.equals(Origin.AUTO) || customReader != null || jsonField != null) {
            cb.add(", $S", originName);
        }

        if (customReader != null || jsonField != null) {
            cb.add(", $T.class", customReader);
        }

        if (jsonField != null) {
            cb.add(", $T::$N",
                    ClassName.bestGuess(jsonField.getParentRequest().getJsonRequestClassName()),
                    jsonField.getterName()
            );
        }

        cb.add(")");
        return cb.build();
    }

    @Override
    protected CodeBlock generateWritingContext(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("new $T(", ClassName.get(RestletTWContext.class));

        TypeName writerClass = getCustomWriterClass(teleMethod);
        if (writerClass != null) {
            cb.add("$T.class", writerClass);
        }
        cb.add(")");
        return cb.build();
    }

    protected TypeName getCustomWriterClass(TeleMethodElement teleMethod) {
        var wrAnn = teleMethod.getServiceMethod().getOriginMethod().getAnnotation(RestletResponseWriter.class);
        if (wrAnn == null) {
            wrAnn = teleMethod.getParentTeleFacade().getParentService().getOriginClass().getAnnotation(RestletResponseWriter.class);
        }
        if (wrAnn == null) {
            return null;
        }
        TypeMirror readerClassMirror = wrAnn.getValueTypeMirror(a -> a.value());
        return TypeName.get(readerClassMirror);
    }

    protected TypeName getCustomReaderClass(TeleParamElement teleParam) {
        var jsonEntryAnn = teleParam.getOriginParam().getAnnotation(JsonField.class);
        if (jsonEntryAnn == null) {
            jsonEntryAnn = teleParam.getParentTeleMethod().getServiceMethod().getOriginMethod().getAnnotation(JsonField.class);
        }
        if (jsonEntryAnn != null) {
            return TypeName.get(CodegenUtils.classToTypeMirror(JsonFieldReader.class, getProcessorContext().getElementUtils()));
        }

        var rdAnn = teleParam.getOriginParam().getAnnotation(RestletParamReader.class);
        if (rdAnn == null) {
            rdAnn = teleParam.getParentTeleMethod().getServiceMethod().getOriginMethod().getAnnotation(RestletParamReader.class);
        }
        if (rdAnn == null) {
            return null;
        }
        TypeMirror readerClassMirror = rdAnn.getValueTypeMirror(a -> a.value());
        return TypeName.get(readerClassMirror);
    }


}
