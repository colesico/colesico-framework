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

package colesico.framework.weblet.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.router.RouterTargetController;
import colesico.framework.telehttp.origin.Origin;
import colesico.framework.weblet.teleapi.*;
import colesico.framework.weblet.teleapi.origin.WebletAutoOrigin;

import jakarta.inject.Singleton;


@Producer
@Produce(WebletTeleControllerImpl.class)
@Produce(value = WebletDataPortImpl.class, keyType = WebletDataPort.class, scoped = Singleton.class)
@Produce(value = WebletAutoOrigin.class, keyType = Origin.class, scoped = Singleton.class, named = WebletOrigin.AUTO)
public class WebletProducer {

    @Singleton
    public WebletTeleController webletTeleController(WebletTeleControllerImpl impl) {
        return impl;
    }

    @Singleton
    public RouterTargetController routerTargetController(WebletTeleController impl) {
        return impl;
    }
}
