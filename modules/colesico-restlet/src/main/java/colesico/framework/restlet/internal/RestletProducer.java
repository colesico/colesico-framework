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

package colesico.framework.restlet.internal;

import colesico.framework.ioc.message.IocMessage;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.restlet.RestletConfigPrototype;
import colesico.framework.restlet.assist.LogRestletListener;
import colesico.framework.restlet.internal.gsonconv.GsonSerializer;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.restlet.teleapi.origin.RestletAutoOrigin;
import colesico.framework.telehttp.origin.Origin;

import jakarta.inject.Named;
import jakarta.inject.Singleton;


/**
 * @author Vladlen Larionov
 */
@Producer
@Produce(RestletDataPortImpl.class)
@Produce(RestletTeleControllerImpl.class)
@Produce(RestletAutoOrigin.class)
@Produce(GsonSerializer.class)
@Produce(LogRestletListener.class)
public class RestletProducer {

    @Singleton
    public RestletDataPort restletDataPort(RestletDataPortImpl impl) {
        return impl;
    }

    @Singleton
    public RestletTeleController restletTeleDriver(RestletTeleControllerImpl impl) {
        return impl;
    }

    //TODO: add support for serializers by content-type
    @Singleton
    public RestletSerializer restletSerializer(@IocMessage String contentType, GsonSerializer impl) {
        return impl;
    }

    @Singleton
    public RestletRequestListener restletRequestListener(LogRestletListener impl) {
        return impl;
    }

    @Singleton
    public RestletResponseListener restletResponseListener(LogRestletListener impl) {
        return impl;
    }

    @Singleton
    public RestletConfigPrototype defaultRestletConfig() {
        return new RestletConfigImpl();
    }

    @Singleton
    @Named(RestletOrigin.AUTO)
    public Origin restletAutoOrigin(RestletAutoOrigin impl) {
        return impl;
    }
}
