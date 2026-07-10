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

import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.restlet.*;
import colesico.framework.restlet.assist.LogRestletListener;
import colesico.framework.restlet.gson.GsonSerializer;

import colesico.framework.httprouter.TargetController;
import jakarta.inject.Singleton;


/**
 * @author Vladlen Larionov
 */
@Producer
@Produce(value = GsonSerializer.class, keyType = JsonSerializer.class)
@Produce(value = RestletDataPortImpl.class, keyType = RestletDataPort.class)
@Produce(value = RestletControllerImpl.class, keyType = RestletController.class)
@Produce(value = LogRestletListener.class, keyType = RestletRequestListener.class, polyproduce = 0)
@Produce(value = LogRestletListener.class, keyType = RestletResponseListener.class, polyproduce = 0)
public class RestletProducer {

    @Singleton
    public RestletConfigPrototype defaultRestletConfig() {
        return new RestletConfigImpl();
    }

    @Singleton
    @Polyproduce
    public TargetController routerTargetController(RestletController impl) {
        return impl;
    }
}
