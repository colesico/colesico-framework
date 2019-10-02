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

package colesico.framework.ioc;

import colesico.framework.ioc.internal.IocBuilderImpl;
import colesico.framework.ioc.ioclet.Ioclet;
import org.apache.commons.lang3.StringUtils;

/**
 * IoC container builder interface
 * <p>
 *
 * @author Vladlen Larionov
 */
public interface IocBuilder {

    String IOC_PROFILE_PROPERTY = "colesico.framework.ioc.profile";

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
     * Prevent factories activation after container build.
     * By default builder activates the factories after IoC container has been created.
     *
     * @return
     * @see ContainerType
     */
    IocBuilder disablePreactivation();

    /**
     * Configure IocType
     * <p>
     * Default - EAGER
     *
     * @param val
     * @return
     * @see ContainerType
     */
    IocBuilder useContainerType(ContainerType val);

    /**
     * Do not use ioclet for specified producer
     *
     * @param producerId
     * @return
     */
    IocBuilder ignoreProducer(String producerId);

    static IocBuilder get() {
        return new IocBuilderImpl();
    }

    /**
     * Builds an IoC container instance based on builder configuration
     *
     * @return
     */
    Ioc build();

    /**
     * Creates default production ready IoC container
     *
     * @return
     */
    static Ioc forProduction() {
        return get().build();
    }

    /**
     * Creates default development ready IoC container
     *
     * @return
     */
    static Ioc forDevelopment() {
        return get().useContainerType(ContainerType.LAZY).disablePreactivation().build();
    }

    /**
     * Creates default testing ready IoC container
     *
     * @return
     */

    static Ioc forTests() {
        return get().useContainerType(ContainerType.LAZY).useRank(Rank.RANK_TEST).build();
    }

    /**
     * IoC container instance type.
     * Two types of containers are supported - for the productive environment and for development/testing.
     */
    enum ContainerType {
        /**
         * For productive environment
         * Factories must be activated by builder once at the IoC container instance creation
         */
        EAGER,
        /**
         * For development/testing process
         * A request to activate a factory is performed every time the factory is called
         */
        LAZY
    }
}
