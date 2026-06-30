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

package colesico.framework.weblet;

import colesico.framework.telehttp.TeleHttpReadOptions;
import colesico.framework.telehttp.TeleHttpReader;

import java.lang.reflect.Type;

/**
 * Weblet read options
 *
 * @param customReader Custom reader class or null. If null - default reader will be used to  read the parameter
 * @author Vladlen Larionov
 */
public record WebletReadOptions(
        Type baseType,
        String paramName,
        String originName,
        Class<? extends TeleHttpReader<?, ?>> customReader,
        Object metadata
) implements TeleHttpReadOptions<TeleHttpReader<?, ?>> {

    public static final String OF_METHOD = "of";

    public static WebletReadOptions of(Type baseType, String paramName) {
        return new WebletReadOptions(baseType, paramName, WebletOrigin.AUTO, null, null);
    }

    public static WebletReadOptions of(Type baseType, String paramName, String originName) {
        return new WebletReadOptions(baseType, paramName, originName, null, null);
    }

    public static WebletReadOptions of(Type baseType, String paramName, String originName, Class<? extends WebletTeleReader<?>> readerClass) {
        return new WebletReadOptions(baseType, paramName, originName, readerClass, null);
    }
}
