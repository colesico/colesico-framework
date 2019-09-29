/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License";
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
package colesico.framework.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Http method type and dictionary
 *
 * @author Vladlen Larionov
 */
public final class HttpMethod {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String HEAD = "HEAD";
    public static final String TRACE = "TRACE";
    public static final String PATCH = "PATCH";
    public static final String CONNECT = "CONNECT";
    public static final String OPTIONS = "OPTIONS";

    public static final HttpMethod HTTP_METHOD_GET = new HttpMethod(GET);
    public static final HttpMethod HTTP_METHOD_POST = new HttpMethod(POST);
    public static final HttpMethod HTTP_METHOD_PUT = new HttpMethod(PUT);
    public static final HttpMethod HTTP_METHOD_DELETE = new HttpMethod(DELETE);
    public static final HttpMethod HTTP_METHOD_HEAD = new HttpMethod(HEAD);
    public static final HttpMethod HTTP_METHOD_TRACE = new HttpMethod(TRACE);
    public static final HttpMethod HTTP_METHOD_PATCH = new HttpMethod(PATCH);
    public static final HttpMethod HTTP_METHOD_CONNECT = new HttpMethod(CONNECT);
    public static final HttpMethod HTTP_METHOD_OPTIONS = new HttpMethod(OPTIONS);

    public static HttpMethod of(String name) {
        return new HttpMethod(name);
    }

    private final String name;

    private HttpMethod(String name) {
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("HTTP method name is blank");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HttpMethod that = (HttpMethod) o;

        return new EqualsBuilder()
            .append(name, that.name)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(name)
            .toHashCode();
    }
}
