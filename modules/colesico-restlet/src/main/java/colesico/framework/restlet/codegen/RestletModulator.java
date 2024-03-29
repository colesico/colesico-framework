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
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.restlet.Restlet;
import colesico.framework.restlet.codegen.assist.RestletCodegenUtils;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.router.codegen.RouterTeleFacadeElement;
import colesico.framework.router.codegen.RoutesModulator;
import colesico.framework.service.codegen.assist.ServiceCodegenUtils;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.teleapi.*;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.codegen.HttpTRContextElement;
import colesico.framework.telehttp.codegen.HttpTWContextElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.DeclaredType;
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
        // Enable batch params
        teleFacade.setBatchParams(true);
        return teleFacade;
    }

    @Override
    protected TRContextElement createReadingContext(TeleParameterElement teleParam) {

        String paramName = RestletCodegenUtils.getParamName(teleParam);

        CodeBlock.Builder cb = CodeBlock.builder();

        // new RestletTRContext(
        cb.add("$T.$N(", ClassName.get(RestletTRContext.class), RestletTRContext.OF_METHOD);

        ServiceCodegenUtils.generateTeleEntryType(teleParam, cb);

        cb.add(", $S", paramName);

        String originName = RestletCodegenUtils.getOriginName(teleParam);

        TypeMirror customReader = RestletCodegenUtils.getCustomReaderClass(teleParam, getProcessorContext().getElementUtils());
        ClassType customReaderCT = null;

        if (!originName.equals(RestletOrigin.AUTO) || customReader != null) {
            cb.add(", $S", originName);
        }

        if (customReader != null) {
            cb.add(", $T.class", TypeName.get(customReader));
            customReaderCT = new ClassType(getProcessorContext().getProcessingEnv(), (DeclaredType) customReader);
        }

        cb.add(")");

        return new HttpTRContextElement(teleParam, cb.build(), paramName, originName, customReaderCT);
    }

    @Override
    protected TWContextElement createWritingContext(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("$T.$N(", ClassName.get(RestletTWContext.class), RestletTWContext.OF_METHOD);

        ServiceCodegenUtils.generateTeleResultType(teleMethod, cb);

        TypeMirror customWriter = getCustomWriterClass(teleMethod);
        ClassType customWriterCT = null;
        if (customWriter != null) {
            cb.add(", $T.class", TypeName.get(customWriter));
            customWriterCT = new ClassType(getProcessorContext().getProcessingEnv(), (DeclaredType) customWriter);
        }
        cb.add(")");
        return new HttpTWContextElement(teleMethod, cb.build(), customWriterCT);
    }

    protected TypeMirror getCustomWriterClass(TeleMethodElement teleMethod) {
        var wrAnn = teleMethod.getServiceMethod().getOriginMethod().getAnnotation(RestletResponseWriter.class);
        if (wrAnn == null) {
            wrAnn = teleMethod.getParentTeleFacade().getParentService().getOriginClass().getAnnotation(RestletResponseWriter.class);
        }
        if (wrAnn == null) {
            return null;
        }
        return wrAnn.getValueTypeMirror(a -> a.value());
    }

    @Override
    public void onTeleEntryParsed(TeleEntryElement teleEntry) {
        super.onTeleEntryParsed(teleEntry);
        if (teleEntry instanceof TeleBatchFieldElement) {
            AnnotationAssist<ParamName> paramNameAnn = teleEntry.getOriginElement().getAnnotation(ParamName.class);
            if (paramNameAnn == null) {
                return;
            }
            ((TeleBatchFieldElement) teleEntry).setName(paramNameAnn.unwrap().value());
        }
    }
}
