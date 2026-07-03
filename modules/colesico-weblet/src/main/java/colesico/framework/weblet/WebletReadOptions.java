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

import colesico.framework.telehttp.HttpReader;
import colesico.framework.telehttp.HttpReadOptions;

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
        Class<? extends HttpReader<?, ?>> customReader,
        Object metadata
) implements HttpReadOptions {

    public static final String BUILDER_METHOD = "builder";
    public static final String BUILD_METHOD = "build";
    public static final String PARAM_NAME_METHOD = "paramName";
    public static final String ORIGIN_NAME_METHOD = "originName";
    public static final String CUSTOM_READER_METHOD = "customReader";

    @Deprecated
    public WebletReadOptions(Type baseType, String paramName, String originName, Class<? extends HttpReader<?, ?>> customReader, Object metadata) {
        this.baseType = baseType;
        this.paramName = paramName;
        this.originName = originName;
        this.customReader = customReader;
        this.metadata = metadata;
    }

    public static Builder builder(Type baseType) {
        return new Builder(baseType);
    }

    public static class Builder {
        private final Type baseType;
        private String paramName;
        private String originName;
        private Class<? extends HttpReader<?, ?>> customReader;
        private Object metadata;

        public Builder(Type baseType) {
            this.baseType = baseType;
        }

        public WebletReadOptions build() {
            return new WebletReadOptions(baseType, paramName, originName, customReader, metadata);
        }

        public Builder paramName(String paramName) {
            this.paramName = paramName;
            return this;
        }

        public Builder originName(String originName) {
            this.originName = originName;
            return this;
        }

        public Builder customReader(Class<? extends HttpReader<?, ?>> customReader) {
            this.customReader = customReader;
            return this;
        }

        public Builder metadata(Object metadata) {
            this.metadata = metadata;
            return this;
        }
    }
}
