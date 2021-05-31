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

package colesico.framework.telehttp;

/**
 * Origin type and basic dictionary.
 * Origin defines strategy for reading param value from http context
 */
public interface Origin<K, V> {
    /**
     * Read value from request url path
     */
    String ROUTE = "ROUTE";

    /**
     * Read value from url query string
     */
    String QUERY = "QUERY";

    /**
     * From request body post params
     */
    String POST = "POST";

    /**
     * Post request file
     */
    String FILE = "FILE";

    /**
     * From request body
     */
    String BODY = "BODY";

    /**
     * From http header
     */
    String HEADER = "HEADER";

    /**
     * From cookie value
     */
    String COOKIE = "COOKIE";

    V getValue(K key);

}
