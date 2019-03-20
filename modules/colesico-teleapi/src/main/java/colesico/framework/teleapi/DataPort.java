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

import java.lang.reflect.Type;

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

    String READ_FOR_CLASS_METHOD = "readForClass";
    String READ_FOR_TYPE_METHOD = "readForType";
    String WRITE_FOR_CLASS_METHOD = "writeForClass";
    String WRITE_FOR_TYPE_METHOD = "writeForType";

    /**
     * Key for storing instance of TeleDataPort in process scope
     */
    Key<DataPort> SCOPE_KEY = new TypeKey<>(DataPort.class);

    /**
     * Read value from client request.
     * This method should be used with generic types.
     *
     * @param valueType
     * @param context
     * @param <V>
     * @return
     */
    <V> V readForType(Type valueType, R context);

    /**
     * Read value from client request
     *
     * @param valueClass
     * @param context    value reading context. Is used for value reading customization
     * @param <V>
     * @return
     */
    default <V> V readForClass(Class<V> valueClass, R context) {
        return readForType(valueClass, context);
    }

    /**
     * Writes data to the client response.
     * This method should be used with generic types.
     *
     * @param valueType
     * @param value
     * @param context
     * @param <V>
     */
    <V> void writeForType(Type valueType, V value, W context);

    /**
     * Writes value to the client response
     *
     * @param value
     * @param context value writing context. Is used for value writing customization
     * @param <V>
     */
    default <V> void writeForClass(Class<V> valueClass, V value, W context) {
        writeForType(valueClass, value, context);
    }
}
