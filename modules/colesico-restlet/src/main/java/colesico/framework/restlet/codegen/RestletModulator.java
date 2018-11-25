/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.restlet.codegen;


import colesico.framework.assist.CollectionUtils;
import colesico.framework.restlet.Restlet;
import colesico.framework.restlet.teleapi.RestletDataPort;
import colesico.framework.restlet.teleapi.RestletTeleDriver;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;
import colesico.framework.weblet.codegen.WebletModulator;
import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
@AutoService(Modulator.class)
public class RestletModulator extends WebletModulator {

    @Override
    protected Class<? extends Annotation> getTeleAnnotation() {
        return Restlet.class;
    }

    @Override
    protected String getTeleType() {
        return Restlet.class.getSimpleName();
    }

    @Override
    protected Class<? extends TeleDriver> getTeleDriverClass() {
        return RestletTeleDriver.class;
    }

    @Override
    protected Class<? extends DataPort> getDataPortClass() {
        return RestletDataPort.class;
    }

    @Override
    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return CollectionUtils.annotationClassSet(Restlet.class);
    }
}
