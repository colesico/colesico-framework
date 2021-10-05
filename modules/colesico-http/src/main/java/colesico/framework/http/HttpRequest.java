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
package colesico.framework.http;

import java.io.InputStream;
import java.io.Writer;
import java.util.Map;

/**
 * Http request model
 */
public interface HttpRequest {

    /**
     * Returns the http method. e.g. GET, POST, etc.
     */
    HttpMethod getRequestMethod();

    /**
     * Http protocol string: http, https etc.
     */
    String getRequestScheme();

    /**
     * Requested host
     */
    String getHost();

    /**
     * Requested port
     */
    Integer getPort();

    /**
     * The part of request's URL from the domain(port) name up to the query string
     */
    String getRequestURI();

    /**
     * Query URL part after '?' char
     */
    String getQueryString();

    /**
     * Returns read-only http headers
     */
    HttpValues<String, String> getHeaders();

    /**
     * Returns read-only cookies
     */
    HttpValues<String, HttpCookie> getCookies();

    /**
     * Returns read-only query string parameters
     */
    HttpValues<String,String> getQueryParameters();

    /**
     * Returns read-only string parameters model from post map with Content-Type: application/x-www-form-urlencoded
     * or  multipart/form-data except files
     */
    HttpValues<String,String> getPostParameters();

    /**
     * Returns read-only file model from post map with Content-Type: multipart/form-data
     */
    HttpValues<String, HttpFile> getPostFiles();

    /**
     * Returns body input stream
       */
    InputStream getInputStream();

    /**
     * Dump request data to characters output for further logging
     */
    void dump(Writer out);
}
