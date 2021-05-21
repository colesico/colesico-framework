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

package colesico.framework.restlet.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.restlet.RestletConfigPrototype;
import colesico.framework.restlet.assist.LogRestletListener;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.restlet.internal.gsonconv.GsonConverter;
import colesico.framework.restlet.teleapi.jsonrequest.JsonRequest;

import javax.inject.Singleton;


/**
 * @author Vladlen Larionov
 */
@Producer
@Produce(JsonRequestFactory.class)
@Produce(RestletDataPortImpl.class)
@Produce(RestletTeleDriverImpl.class)
@Produce(GsonConverter.class)
@Produce(LogRestletListener.class)
public class RestletProducer {

    @Singleton
    public RestletDataPort getRestletDataPort(RestletDataPortImpl impl) {
        return impl;
    }

    @Singleton
    public RestletTeleDriver getRestletTeleDriver(RestletTeleDriverImpl impl) {
        return impl;
    }

    @Singleton
    public RestletJsonConverter getJsonConverter(GsonConverter impl) {
        return impl;
    }

    @Singleton
    public RestletRequestListener getRestletRequestListener(LogRestletListener impl) {
        return impl;
    }

    @Singleton
    public RestletResponseListener getRestletResponseListener(LogRestletListener impl) {
        return impl;
    }

    @Singleton
    public RestletConfigPrototype getDefaultRestletConfig() {
        return new RestletConfigImpl();
    }

    public JsonRequest getJsonMapContext(ThreadScope scope) {
        return scope.get(JsonRequest.SCOPE_KEY);
    }
}
