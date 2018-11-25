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
package colesico.framework.fluentjdbc.internal;

import colesico.framework.fluentjdbc.Fjdbc;
import colesico.framework.fluentjdbc.FjdbcConfig;
import colesico.framework.ioc.*;

import javax.inject.Singleton;

import static colesico.framework.ioc.Rank.RANK_MINOR;

/**
 * Dagger partition for dispatcher service group
 *
 * @author Vladlen Larionov
 */
@Producer(RANK_MINOR)
@Produce(FjdbcImpl.class)
@Produce(FjdbcConfigImpl.class)
public class FjdbcProducer {

    /**
     * Fjdbc factory
     *
     * @param impl
     * @return
     */

    @Classed(FjdbcConfig.class)
    @Unscoped
    public Fjdbc fjdbcFactory(FjdbcImpl impl) {
        return impl;
    }


    /**
     * Default Fjdbc
     *
     * @return
     */
    @Singleton
    public Fjdbc getDefaultFjdbc(@Classed(FjdbcConfig.class) Supplier<Fjdbc> factory, FjdbcConfigImpl config) {
        return factory.get(config);
    }
}
