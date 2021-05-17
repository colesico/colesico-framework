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

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Origin type and basic dictionary.
 * Origin defines strategy for reading param value from http context
 */
public class Origin {
    /**
     * Read value from request url path
     */
    public static final String ROUTE = "ROUTE";

    /**
     * Read value from url query string
     */
    public static final String QUERY = "QUERY";

    /**
     * From request body post params
     */
    public static final String POST = "POST";

    /**
     * From request body
     */
    public static final String BODY = "BODY";

    /**
     * From http header
     */
    public static final String HEADER = "HEADER";

    /**
     * From cookie value
     */
    public static final String COOKIE = "COOKIE";

    /**
     * Strategy depends on tele data port implementation.
     */
    public static final String AUTO = "AUTO";

    public static final Origin ORIGIN_ROUTE = of(ROUTE);
    public static final Origin ORIGIN_QUERY = of(QUERY);
    public static final Origin ORIGIN_POST = of(POST);
    public static final Origin ORIGIN_BODY = of(BODY);
    public static final Origin ORIGIN_HEADER = of(HEADER);
    public static final Origin ORIGIN_COOKIE = of(COOKIE);
    public static final Origin ORIGIN_AUTO = of(AUTO);

    public static Origin of(String name) {
        return new Origin(name);
    }

    private final String name;

    protected Origin(String name) {
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("Origin name is blank");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Origin." + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Origin origin = (Origin) o;
        return name.equals(origin.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
