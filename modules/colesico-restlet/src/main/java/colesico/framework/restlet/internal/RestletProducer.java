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

package colesico.framework.restlet.internal;

import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.restlet.teleapi.RestletDataPort;
import colesico.framework.restlet.teleapi.RestletTeleDriver;
import colesico.framework.restlet.teleapi.converter.JsonConverter;
import colesico.framework.restlet.teleapi.converter.JsonIterConverter;

import javax.inject.Singleton;

import static colesico.framework.ioc.Rank.RANK_MINOR;

/**
 * @author Vladlen Larionov
 */
@Producer(RANK_MINOR)
@Produce(RestletDataPortImpl.class)
@Produce(RestletTeleDriverImpl.class)
@Produce(JsonIterConverter.class)
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
    public JsonConverter getJsonConverter(JsonIterConverter impl) {
        return impl;
    }

}
