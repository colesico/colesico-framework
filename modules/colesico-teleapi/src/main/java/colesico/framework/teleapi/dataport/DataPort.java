/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.teleapi.dataport;


import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

import java.lang.reflect.Type;

/**
 * Data exchange port for communication with remote client.
 * This port is designed for retrieving parameters values from remote client and sending back a results.
 * A request "controller"  (tele-driver, controller servlet or something similar) creates and puts DataPort instance
 * to the process scope for each request process.
 *
 * @param <R> Data reading context
 * @param <W> Data writing context
 */
public interface DataPort<R extends TRContext<?, ?>, W extends TWContext<?, ?>> {

    String READ_METHOD = "read";
    String WRITE_METHOD = "write";

    /**
     * Key for storing instance of DataPort in process scope
     */
    Key<DataPort> SCOPE_KEY = new TypeKey<>(DataPort.class);

    /**
     * Read value from remote request.
     *
     * @param context data reading context
     * @param <V>     reading value type
     */
    <V> V read(R context);

    /**
     * Read value by type from remote request.
     * Internally must create appropriate reading context and forward to {@link DataPort#read(R)}
     */
    <V, A> V read(Type valueType, A attributes);

    default <V> V read(Type valueType) {
        return read(valueType, null);
    }

    /**
     * Writes data to the remote response.
     *
     * @param context data writing context
     * @param <V>     writing value type
     */
    <V> void write(V value, W context);

    /**
     * Write data by type to the response
     * Internally must create appropriate writing context and forward to {@link #write(V, Type)}
     */
    <V, A> void write(V value, Type valueType, A attributes);

    default <V> void write(V value, Type valueType) {
        write(value, valueType, null);
    }

}
