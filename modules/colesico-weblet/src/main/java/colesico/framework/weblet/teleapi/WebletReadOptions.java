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

import colesico.framework.telehttp.HttpReadOptions;

/**
 * Weblet read options
 *
 * @param readerClass Custom reader class or null. If null - default reader will be used to  read the parameter
 * @author Vladlen Larionov
 */
public record WebletReadOptions(
        String paramName,
        String originName,
        Class<? extends WebletTeleReader<?>> readerClass,
        Object attachment
) implements HttpReadOptions {

    public static final String OF_METHOD = "of";

    public static WebletReadOptions of() {
        return new WebletReadOptions(null, null, null, null);
    }

    public static WebletReadOptions of(Object attachment) {
        return new WebletReadOptions(null, null, null, attachment);
    }

    public static WebletReadOptions of(String paramName) {
        return new WebletReadOptions(paramName, WebletOrigin.AUTO, null, null);
    }

    public static WebletReadOptions of(String paramName, String originName) {
        return new WebletReadOptions(paramName, originName, null, null);
    }

    public static WebletReadOptions of(String paramName, String originName, Class<? extends WebletTeleReader<?>> readerClass) {
        return new WebletReadOptions(paramName, originName, readerClass, null);
    }
}
