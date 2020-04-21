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
package colesico.framework.router.internal;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.Router;
import colesico.framework.router.RouterBuilder;
import colesico.framework.router.RouterContext;
import colesico.framework.router.RouterOptions;

import javax.inject.Singleton;


/**
 * @author Vladlen Larionov
 */
@Producer
@Produce(RouterBuilderImpl.class)
public class RouterProducer {

    @Singleton
    public RouterBuilder getRouterBuilder(final RouterBuilderImpl impl, Polysupplier<RouterOptions> options) {
        options.forEach(o -> o.applyOptions(impl), null);
        return impl;
    }

    @Singleton
    public Router getRouter(RouterBuilder builder) {
        return builder.build();
    }

    public RouterContext getActionContext(ThreadScope threadScope) {
        return threadScope.get(RouterContext.SCOPE_KEY);
    }

}
