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
import colesico.framework.assist.codegen.model.AnnotationToolbox;
import colesico.framework.restlet.Restlet;
import colesico.framework.restlet.teleapi.RestletTIContext;
import colesico.framework.restlet.teleapi.RestletDataPort;
import colesico.framework.restlet.teleapi.RestletTeleDriver;
import colesico.framework.router.codegen.RoutesModulator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.weblet.teleapi.WebletTDRContext;
import colesico.framework.weblet.teleapi.WebletTDWContext;
import com.squareup.javapoet.CodeBlock;

import java.lang.annotation.Annotation;
import java.util.Set;

import static colesico.framework.weblet.codegen.WebletModulator.generateReadingContextImpl;

/**
 * @author Vladlen Larionov
 */
public class RestletModulator extends
        RoutesModulator<RestletTeleDriver, RestletDataPort, WebletTDRContext, WebletTDWContext, RestletTIContext> {

    @Override
    protected String getTeleType() {
        return Restlet.class.getSimpleName();
    }

    @Override
    protected boolean isTeleFacadeSupported(ServiceElement serviceElm) {
        AnnotationToolbox teleAnn = serviceElm.getOriginClass().getAnnotation(Restlet.class);
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
        return generateReadingContextImpl(teleParam);
    }
}
