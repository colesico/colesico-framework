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

import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Weblet write options
 *
 * @param customWriter Custom writer class or null. If null - default writer will be used
 * @author Vladlen Larionov
 */
public record WebletWriteOptions(
        Type baseType,
        Integer statusCode,
        ContentType contentType,
        Charset charset,
        Class<? extends TeleHttpWriter<?, ?>> customWriter,
        Object metadata
) implements TeleHttpWriteOptions {

    public static final String OF_METHOD = "of";

    public static WebletWriteOptions of() {
        return new WebletWriteOptions(
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static WebletWriteOptions of(Type baseType) {
        return new WebletWriteOptions(
                baseType,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static WebletWriteOptions of(Type baseType, Object metadata) {
        return new WebletWriteOptions(
                baseType,
                null,
                null,
                null,
                null,
                metadata
        );
    }


    public static WebletWriteOptions of(Type baseType, Class<? extends WebletTeleWriter<?>> writerClass) {
        return new WebletWriteOptions(
                baseType,
                null,
                null,
                null,
                writerClass,
                null
        );
    }
}
