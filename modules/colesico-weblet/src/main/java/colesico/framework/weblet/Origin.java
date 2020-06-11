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

package colesico.framework.weblet;

/**
 * Origin types to read a parameter value from them.
 * This enums defines default strategy for reading param values from http context
 */
public enum Origin {
    /**
     * From url path
     */
    ROUTE,

    /**
     * From url query string
     */
    QUERY,

    /**
     * From request body post params
     */
    POST,

    /**
     * From request body
     */
    BODY,

    /**
     * From http header
     */
    HEADER,

    /**
     * From cookie value
     */
    COOKIE,

    /**
     * Strategy depends on data port implementation.
     * <p>
     * Default behavior: for GET, HEAD requests - from query,route params;
     * for POST,PUT,PATCH - from post,query,route params
     */
    AUTO
}
