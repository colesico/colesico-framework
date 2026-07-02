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
        Class<? extends TeleHttpWriter<?, ?>> customWriter,
        Object metadata
) implements TeleHttpWriteOptions {

    public static final String BUILDER_METHOD = "builder";
    public static final String BUILD_METHOD = "build";
    public static final String CUSTOM_WRITER_METHOD = "customWriter";

    @Deprecated
    public WebletWriteOptions(Type baseType, Integer statusCode, ContentType contentType, Class<? extends TeleHttpWriter<?, ?>> customWriter, Object metadata) {
        this.baseType = baseType;
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.customWriter = customWriter;
        this.metadata = metadata;
    }

    public static Builder builder(Type baseType) {
        return new Builder(baseType);
    }

    public static class Builder {
        private final Type baseType;
        private Integer statusCode;
        private ContentType contentType;
        private Class<? extends TeleHttpWriter<?, ?>> customWriter;
        private Object metadata;

        public Builder(Type baseType) {
            this.baseType = baseType;
        }

        public WebletWriteOptions build() {
            return new WebletWriteOptions(baseType, statusCode, contentType, customWriter, metadata);
        }

        public Builder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder customWriter(Class<? extends TeleHttpWriter<?, ?>> customWriter) {
            this.customWriter = customWriter;
            return this;
        }

        public Builder metadata(Object metadata) {
            this.metadata = metadata;
            return this;
        }
    }
}
