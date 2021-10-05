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
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.restlet.Restlet;
import colesico.framework.restlet.codegen.assist.RestletCodegenUtils;
import colesico.framework.restlet.codegen.model.JsonFieldElement;
import colesico.framework.restlet.codegen.model.JsonRequestElement;
import colesico.framework.restlet.codegen.model.JsonPackElement;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.restlet.teleapi.jsonrequest.JsonField;
import colesico.framework.router.codegen.RouterTeleFacadeElement;
import colesico.framework.router.codegen.RoutesModulator;
import colesico.framework.service.codegen.assist.ServiceCodegenUtils;
import colesico.framework.service.codegen.model.*;
import com.squareup.javapoet.*;

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
    protected boolean isTeleFacadeSupported(ServiceElement service) {
        AnnotationAssist teleAnn = service.getOriginClass().getAnnotation(Restlet.class);
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
     * Assign json request pack element to telefacade
     */
    @Override
    protected RouterTeleFacadeElement createTeleFacade(ServiceElement serviceElm) {
        RouterTeleFacadeElement teleFacade = super.createTeleFacade(serviceElm);
        JsonPackElement jrPak = new JsonPackElement(teleFacade);
        teleFacade.setProperty(JsonPackElement.class, jrPak);
        return teleFacade;
    }

    /**
     * Generate parameters wrapper class
     *
     * @param teleMethodElement
     */
    @Override
    protected void processTeleMethod(TeleMethodElement teleMethodElement) {
        super.processTeleMethod(teleMethodElement);

        var jsonPack = teleMethodElement.getParentTeleFacade().getProperty(JsonPackElement.class);
        JsonRequestElement jsonRequest = null;

        final var jsonMethodAnn = teleMethodElement.getServiceMethod().getOriginMethod().getAnnotation(JsonField.class);
        if (jsonMethodAnn != null) {
            jsonRequest = new JsonRequestElement(teleMethodElement);
            jsonPack.addRequest(jsonRequest);
            teleMethodElement.setProperty(JsonRequestElement.class, jsonRequest);
        }

        for (TeleArgumentElement teleArg : teleMethodElement.getParameters()) {

            var jsonParamAnn = teleArg.getOriginElement().getAnnotation(JsonField.class);

            // Skip compound
            if (teleArg instanceof TeleCompoundElement) {
                if (jsonParamAnn != null) {
                    throw CodegenException.of()
                            .message("Compounds are not supported for json fields")
                            .element(teleArg.getOriginElement().unwrap())
                            .build();
                }
                continue;
            }

            TeleParameterElement teleParam = (TeleParameterElement) teleArg;

            if (jsonMethodAnn != null || jsonParamAnn != null) {

                if (jsonRequest == null) {
                    jsonRequest = new JsonRequestElement(teleMethodElement);
                    jsonPack.addRequest(jsonRequest);
                    teleMethodElement.setProperty(JsonRequestElement.class, jsonRequest);
                }

                JsonFieldElement jsonField = new JsonFieldElement(teleParam);
                jsonRequest.addField(jsonField);
                teleParam.setProperty(JsonFieldElement.class, jsonField);
            }
        }

    }

    @Override
    protected void processTeleFacade(TeleFacadeElement teleFacadeElement) {
        super.processTeleFacade(teleFacadeElement);
        var jsonPack = teleFacadeElement.getProperty(JsonPackElement.class);
        if (jsonPack.getRequests().isEmpty()) {
            return;
        }

        JsonPackGenerator jsonPackGenerator = new JsonPackGenerator(getProcessorContext().getProcessingEnv());
        jsonPackGenerator.generate(jsonPack);
    }

    @Override
    protected CodeBlock generateReadingContext(TeleParameterElement teleParam) {
        String paramName = RestletCodegenUtils.getParamName(teleParam);

        CodeBlock.Builder cb = CodeBlock.builder();

        // new RestletTRContext(paramName
        cb.add("$T.$N(", ClassName.get(RestletTRContext.class), RestletTRContext.OF_METHOD);

        ServiceCodegenUtils.generateTeleArgumentType(teleParam, cb);

        cb.add(", $S", paramName);

        String originName = RestletCodegenUtils.getOriginName(teleParam);

        JsonFieldElement jsonField = teleParam.getProperty(JsonFieldElement.class);

        TypeName customReader = RestletCodegenUtils.getCustomReaderClass(teleParam, getProcessorContext().getElementUtils());
        if (!originName.equals(RestletOrigin.AUTO) || customReader != null || jsonField != null) {
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
        cb.add("$T.$N(", ClassName.get(RestletTWContext.class), RestletTWContext.OF_METHOD);

        ServiceCodegenUtils.generateTeleResultType(teleMethod, cb);

        TypeName writerClass = getCustomWriterClass(teleMethod);
        if (writerClass != null) {
            cb.add(", $T.class", writerClass);
        }
        cb.add(")");
        return cb.build();
    }

    @Override
    protected CodeBlock generateInvocationContext(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        JsonRequestElement jsonRequest = teleMethod.getProperty(JsonRequestElement.class);
        if (jsonRequest != null) {
            cb.add("$T.$N(", ClassName.get(RestletTIContext.class), RestletTIContext.OF_METHOD);
            cb.add("$T.class", ClassName.bestGuess(jsonRequest.getJsonRequestClassName()));
            cb.add(")");
        } else {
            cb.add("null");
        }
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


}
