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

package colesico.framework.ioc.ioclet;

/**
 * IoC container module that provides the factories to the IoC container.
 * <p>
 *
 * @author Vladlen Larionov
 */
public interface Ioclet {
    String GET_PRODUCER_ID_METHOD = "getProducerId";
    String GET_RANK_METHOD = "getRank";
    String ADD_FACTORIES_METHOD = "addFactories";
    String CATALOG_PARAM = "catalog";

    /**
     * Unique producer ID on which the ioclet is based on.
     * Typically it is a full name of producer class
     *
     * @return
     * @see colesico.framework.ioc.Producer
     */
    String getProducerId();

    /**
     * Rank of Ioclet. Obtained from producer definition
     *
     * @return
     * @see colesico.framework.ioc.Rank
     */
    String getRank();

    /**
     * This method implementation should register the factories
     *
     * @param catalog
     */
    void addFactories(final Catalog catalog);

}
