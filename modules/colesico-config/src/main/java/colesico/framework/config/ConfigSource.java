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

package colesico.framework.config;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Configuration source.
 * Supplies an unified api to obtain configuration values from any source (file, database, http resource, etc.)
 */
public interface ConfigSource {

    /**
     * Default parameter name for config source.
     * This parameter name should be used by code generator when the only parameter
     * is passed with {@link SourceOption}
     *
     * @see UseSource
     */
    String CONNECT_METHOD = "connect";

    /**
     * Get connection to configuration source
      */
    Connection connect(Map<String, String> params);

    /**
     * Represents config source connection
     */
    interface Connection {
        String GET_VALUE_METHOD = "getValue";
        String CLOSE_METHOD = "close";

        /**
         * Returns a configuration value of given type
         *
         * @param valueType actual value type to convert to it.
         * @param <T> config value type
         * @return null if value not found, value otherwise
         */
        <T> T getValue(Type valueType);

        /**
         * Close connection
         */
        void close();
    }
}
