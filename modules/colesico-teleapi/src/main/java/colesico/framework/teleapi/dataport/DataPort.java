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

/**
 * Data port for data exchange with remote source.
 */
public interface DataPort<R extends ReadOptions, W extends WriteOptions> {

    String READ_METHOD = "read";
    String WRITE_METHOD = "write";

    /**
     * Key for storing instance of DataPort in a scope
     */
    Key<DataPort> SCOPE_KEY = new TypeKey<>(DataPort.class);

    /**
     * Read value from remote source.
     */
    <V> V read(Class<V> valueType, R options);

    /**
     * Read value from remote source.
     * Internally must create appropriate {@link ReadOptions} and forward
     * to {@link #read(Class, ReadOptions)}
     *
     * @param attachment see {@link ReadOptions#attachment()}
     */
    <V> V read(Class<V> valueType, Object attachment);

    default <V> V read(Class<V> valueType) {
        return read(valueType, null);
    }

    /**
     * Writes value to the remote source.
     */
    <V> void write(V value, Class<V> valueType, W options);

    /**
     * Write value to remote source.
     * Internally must create appropriate {@link WriteOptions} and forward to
     * {@link #write(Object, Class, WriteOptions)}
     *
     * @param attachment see {@link WriteOptions#attachment()}
     */
    <V> void write(V value, Class<V> valueType, Object attachment);

    default <V> void write(V value, Class<V> valueType) {
        write(value, valueType, null);
    }

}
