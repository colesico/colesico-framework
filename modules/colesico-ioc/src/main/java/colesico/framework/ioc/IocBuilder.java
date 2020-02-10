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

package colesico.framework.ioc;

import colesico.framework.ioc.internal.IocBuilderImpl;
import colesico.framework.ioc.ioclet.Ioclet;

/**
 * IoC container builder interface
 * <p>
 *
 * @author Vladlen Larionov
 */
public interface IocBuilder {

    String IOC_PROFILE_PROPERTY = "colesico.framework.ioc.profile";

    /**
     * Creates  production ready IoC container builder
     */
    static IocBuilder forProduction() {
        return IocBuilderImpl.forProduction();
    }

    /**
     * Creates default development ready IoC container builder
     */
    static IocBuilder forDevelopment() {
        return IocBuilderImpl.forDevelopment();
    }

    /**
     * Creates default testing ready IoC container builder
     */
    static IocBuilder forTests() {
        return IocBuilderImpl.forTests();
    }

    /**
     * Add  rank to ranks stack.
     * By default builder use this ranks: 'minor', 'default', 'extension'.
     * With tis method 'test' rank can be added.
     *
     * @param name rank name
     * @return builder instance
     * @see Rank
     */
    IocBuilder useRank(String name);

    /**
     * Disable using default ranks
     *
     * @return
     */
    IocBuilder disableDefaultRanks();

    /**
     * Disable automatic ioclets discovery.
     * By default the builder will search for the ioclets using the ServiceLoader mechanism.
     *
     * @return
     * @see #useIoclet
     */
    IocBuilder disableIocletsDiscovery();

    /**
     * Add custom ioclet to Ioc container
     *
     * @param ioclet
     * @return
     * @see Ioclet
     */
    IocBuilder useIoclet(Ioclet ioclet);

    /**
     * Do not use ioclet for specified producer
     *
     * @param producerId
     */
    IocBuilder ignoreProducer(String producerId);


    /**
     * Builds an IoC container instance based on builder configuration
     *
     * @return
     */
    Ioc build();

}
