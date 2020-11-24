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

package colesico.framework.teleapi;


import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

import java.lang.reflect.Type;

/**
 * Data exchange port for communication with remote client.
 * This port is destined for retrieving parameters values from remote client and sending back a results.
 * A request "controller"  (tele-driver, controller servlet or something similar) creates and puts DataPort instance
 * to the process scope for each request process.
 *
 * @param <R> Data reading context
 * @param <W> Data writing context
 */
public interface DataPort<R, W> {

    String READ_METHOD = "read";
    String WRITE_METHOD = "write";

    /**
     * Key for storing instance of DataPort in process scope
     */
    Key<DataPort> SCOPE_KEY = new TypeKey<>(DataPort.class);

    /**
     * Read value from remote request.
     *
     * @param valueType
     * @param context data reader context
     * @param <V> reading value type
     */
    <V> V read(Type valueType, R context);

    /**
     * Writes data to the remote response.
     *
     * @param context data writer context
     * @param <V> writing value type
     */
    <V> void write(Type valueType, V value, W context);

}
