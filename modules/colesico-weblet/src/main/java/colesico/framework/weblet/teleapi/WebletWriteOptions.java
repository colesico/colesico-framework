/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.weblet.teleapi;

import colesico.framework.telehttp.HttpWriteOptions;

import java.lang.reflect.Type;

/**
 * Weblet write options
 *
 * @author Vladlen Larionov
 */
public final class WebletWriteOptions extends HttpWriteOptions<Type, Object> {

    public static final String OF_METHOD = "of";

    /**
     * Custom writer class or null.
     * If null - default writer will be used
     *
     * @see WebletResponseWriter
     */
    private final Class<? extends WebletTeleWriter<?>> writerClass;

    public WebletWriteOptions(Type valueType, Class<? extends WebletTeleWriter<?>> writerClass, Object payload) {
        super(valueType, payload);
        this.writerClass = writerClass;
    }

    public static WebletWriteOptions of(Type valueType) {
        return new WebletWriteOptions(valueType, null, null);
    }

    public static WebletWriteOptions of(Type valueType, Object payload) {
        return new WebletWriteOptions(valueType, null, payload);
    }

    public static WebletWriteOptions of(Type valueType, Class<? extends WebletTeleWriter<?>> writerClass) {
        return new WebletWriteOptions(valueType, writerClass, null);
    }

    public Class<? extends WebletTeleWriter<?>> writerClass() {
        return writerClass;
    }
}
