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

package colesico.framework.ioc.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.conditional.Condition;
import colesico.framework.ioc.ioclet.*;
import colesico.framework.ioc.key.TypeKey;
import colesico.framework.ioc.scope.ThreadScope;


/**
 * @author Vladlen Larionov
 */

public final class IocIoclet implements Ioclet {

    private Factory<Ioc> getIOCContainerFactory() {
        return new SingletonFactory<>() {
            private Ioc ioc;

            @Override
            public void setup(AdvancedIoc ioc) {
                this.ioc = ioc;
            }

            @Override
            public Ioc create(Object message) {
                return ioc;
            }
        };
    }

    private Factory<ThreadScope> getThreadScopeFactory() {
        return new SingletonFactory<>() {

            @Override
            public ThreadScope create(Object message) {
                return new ThreadScopeImpl();
            }
        };
    }

    @Override
    public String getId() {
        return IocIoclet.class.getName();
    }

    @Override
    public Condition getCondition() {
        return null;
    }


    @Override
    public void addFactories(Catalog catalog) {
        if (catalog.accept(new TypeKey<>(Ioc.class), null, null, false)) {
            catalog.add(getIOCContainerFactory());
        }

        if (catalog.accept(new TypeKey<>(ThreadScope.class), null, null, false)){
            catalog.add(getThreadScopeFactory());
        }
    }

}
