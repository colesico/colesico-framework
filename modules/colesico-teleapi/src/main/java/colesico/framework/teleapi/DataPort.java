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

package colesico.framework.teleapi;


import colesico.framework.ioc.Key;
import colesico.framework.ioc.TypeKey;

/**
 * Data exchange port for communication with remote client.
 * This port is destined for retrieving parameters values from remote client and sending back a results.
 * A request "controller"  (tele-driver, controller servlet or something similar) creates and puts TeleDataPort instance
 * to the process scope for each request process.
 *
 * @param <R>
 * @param <W>
 */
public interface DataPort<R, W> {
    String READ_METHOD = "read";
    String WRITE_METHOD = "write";

    /**
     * Key for storing instance of TeleDataPort in process scope
     */
    Key<DataPort> SCOPE_KEY = new TypeKey<>(DataPort.class);

    /**
     * Read data from client request
     *
     * @param type
     * @param context model reading context. Is used for model read customization
     * @param <V>
     * @return
     */
    <V> V read(Class<V> type, R context);

    /**
     * Writes data to the client response
     *
     * @param value
     * @param context model writing context. Is used for model writing customization
     * @param <V>
     */
    <V> void write(Class<V> type, V value, W context);
}
