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
import colesico.framework.assist.codegen.model.AnnotationToolbox;
import colesico.framework.router.codegen.RoutesModulator;
import colesico.framework.service.codegen.model.*;
import colesico.framework.weblet.Weblet;
import colesico.framework.weblet.teleapi.*;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
public class WebletModulator extends
        RoutesModulator<WebletTeleDriver, WebletDataPort, WebletTRContext, WebletTWContext, WebletTIContext> {

    @Override
    protected String getTeleType() {
        return Weblet.class.getSimpleName();
    }

    @Override
    protected boolean isTeleFacadeSupported(ServiceElement serviceElm) {
        AnnotationToolbox teleAnn = serviceElm.getOriginClass().getAnnotation(Weblet.class);
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
        WebletTRContextCodegen codegen = new WebletTRContextCodegen(teleParam, WebletTRContext.class, WebletOriginFacade.class);
        return codegen.generate();
    }

    @Override
    protected CodeBlock generateWritingContext(TeleMethodElement teleMethod) {
        WebletTWContextCodegen codegen = new WebletTWContextCodegen(teleMethod, WebletTWContext.class);
        return codegen.generate();
    }


}
