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

package colesico.framework.ioc.ioclet;

import colesico.framework.ioc.condition.Condition;
import colesico.framework.ioc.production.Producer;

/**
 * IoC container module that provides the factories to the IoC container.
 * <p>
 *
 * @author Vladlen Larionov
 */
public interface Ioclet {
    String GET_ID_METHOD = "getId";
    String GET_CONDITION_METHOD = "getCondition";
    String ADD_FACTORIES_METHOD = "addFactories";
    String CATALOG_PARAM = "catalog";

    /**
     * Unique ioclet ID.
     * Typically it is a full name of producer class on which the ioclet is based on.
     *
     * @return
     * @see Producer
     */
    String getId();

    /**
     * Returns producer baser condition
     *
     * @see Condition
     */
    Condition getCondition();

    /**
     * This method implementation should register the factories
     *
     * @param catalog catalog instance
     */
    void addFactories(final Catalog catalog);

}
