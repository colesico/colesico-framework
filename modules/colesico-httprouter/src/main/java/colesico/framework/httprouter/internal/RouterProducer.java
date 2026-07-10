/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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
package colesico.framework.httprouter.internal;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.httprouter.Router;
import colesico.framework.httprouter.RouterBuilder;
import colesico.framework.httprouter.RouterContext;
import colesico.framework.httprouter.RouterOptions;

import jakarta.inject.Singleton;


/**
 * @author Vladlen Larionov
 */
@Producer
@Produce(RouterBuilderImpl.class)
public class RouterProducer {

    @Singleton
    public RouterBuilder routerBuilder(final RouterBuilderImpl impl, Polysupplier<RouterOptions> options) {
        options.forEach(o -> o.applyOptions(impl));
        return impl;
    }

    @Singleton
    public Router router(RouterBuilder builder) {
        return builder.build();
    }

    public RouterContext routerContext(TaskScope taskScope) {
        return taskScope.get(RouterContext.SCOPE_KEY);
    }

}
