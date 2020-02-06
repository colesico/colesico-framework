/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package colesico.framework.undertow.internal;

import colesico.framework.httpserver.HttpServer;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.undertow.UndertowConfigPrototype;

import javax.inject.Singleton;

import static colesico.framework.ioc.Rank.RANK_MINOR;

/**
 * @author Vladlen Larionov
 */
@Producer(RANK_MINOR)
@Produce(UndertowRequestProcessor.class)
@Produce(UndertowConfigImpl.class)
@Produce(UndertowHttpServer.class)
public class UndertowProducer {

    @Singleton
    public HttpServer getHttpServer(UndertowHttpServer impl) {
        return impl;
    }

    // Default configuration
    public UndertowConfigPrototype getUndertowConfig(UndertowConfigImpl impl) {
        return impl;
    }

}
