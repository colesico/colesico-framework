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

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Http method type and dictionary
 *
 * @author Vladlen Larionov
 */
public class HttpMethod {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String HEAD = "HEAD";
    public static final String TRACE = "TRACE";
    public static final String PATCH = "PATCH";
    public static final String CONNECT = "CONNECT";
    public static final String OPTIONS = "OPTIONS";

    public static final HttpMethod HTTP_METHOD_GET = of(GET);
    public static final HttpMethod HTTP_METHOD_POST = of(POST);
    public static final HttpMethod HTTP_METHOD_PUT = of(PUT);
    public static final HttpMethod HTTP_METHOD_DELETE = of(DELETE);
    public static final HttpMethod HTTP_METHOD_HEAD = of(HEAD);
    public static final HttpMethod HTTP_METHOD_TRACE = of(TRACE);
    public static final HttpMethod HTTP_METHOD_PATCH = of(PATCH);
    public static final HttpMethod HTTP_METHOD_CONNECT = of(CONNECT);
    public static final HttpMethod HTTP_METHOD_OPTIONS = of(OPTIONS);

    public static HttpMethod of(String name) {
        return new HttpMethod(name);
    }

    private final String name;

    protected HttpMethod(String name) {
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("HTTP method name is blank");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean is(HttpMethod hm) {
        return equals(hm);
    }

    @Override
    public String toString() {
        return "HttpMethod." + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpMethod that = (HttpMethod) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
