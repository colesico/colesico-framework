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
package colesico.framework.http;

import java.io.InputStream;
import java.io.Writer;

/**
 * Http request model
 */
public interface HttpRequest {

    /**
     * Returns the http method. e.g. GET, POST, etc.
     */
    HttpMethod requestMethod();

    /**
     * Http protocol string: http, https etc.
     */
    String requestScheme();

    /**
     * Requested host
     */
    String host();

    /**
     * Requested port
     */
    Integer port();

    /**
     * The part of request's URL from the domain(port) name up to the query string
     */
    String requestURI();

    /**
     * Query URL part after '?' char
     */
    String queryString();

    /**
     * Returns read-only http headers
     */
    HttpValues<String, String> headers();

    /**
     * Returns read-only cookies
     */
    HttpValues<String, HttpCookie> cookies();

    /**
     * Returns read-only query string parameters
     */
    HttpValues<String,String> queryParameters();

    /**
     * Returns read-only string parameters model from post map with Content-Type: application/x-www-form-urlencoded
     * or  multipart/form-data except files
     */
    HttpValues<String,String> postParameters();

    /**
     * Returns read-only file model from post map with Content-Type: multipart/form-data
     */
    HttpValues<String, HttpFile> postFiles();

    /**
     * Returns body input stream
       */
    InputStream inputStream();

    /**
     * Dump request data to characters output for further logging
     */
    void dump(Writer out);
}
