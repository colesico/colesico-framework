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

import colesico.framework.ioc.annotation.Produce;
import colesico.framework.ioc.annotation.Producer;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.Router;
import colesico.framework.router.RouterContext;

import javax.inject.Singleton;

import static colesico.framework.ioc.Rank.RANK_MINOR;

/**
 * @author Vladlen Larionov
 */
@Producer(RANK_MINOR)
@Produce(RouterImpl.class)
public class RouterProducer {

    @Singleton
    public Router getRouter(RouterImpl impl) {
        return impl;
    }

    public RouterContext getActionContext(ThreadScope threadScope) {
        return threadScope.get(RouterContext.SCOPE_KEY);
    }

}
