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

package colesico.framework.weblet.internal;

import colesico.framework.teleapi.dataport.TeleFactory;
import colesico.framework.weblet.response.WebletResponse;
import colesico.framework.weblet.teleapi.*;

import jakarta.inject.Singleton;

@Singleton
public class WebletDataPortImpl implements WebletDataPort {

    protected final TeleFactory factory;

    public WebletDataPortImpl(TeleFactory factory) {
        this.factory = factory;
    }

    @Override
    public <V> V read(Class<V> valueType, WebletReadOptions options) {
        WebletTeleReader<V> reader;
        if (options.readerClass() != null) {
            // Get specified reader
            reader = (WebletTeleReader<V>) factory.reader(options.readerClass());
        } else {
            // Get reader by value type
            reader = factory.findReader(WebletTeleReader.class, valueType);
            if (reader == null) {
                // Get default reader
                reader = factory.reader(WebletTeleReader.class, Object.class);
            }
        }
        return reader.read(valueType, options);
    }

    @Override
    public <V> V read(Class<V> valueType) {
        return read(valueType, WebletReadOptions.of());
    }

    @Override
    public <V> V read(Class<V> valueType, Object attachment) {
        return read(valueType, WebletReadOptions.of(attachment));
    }

    @Override
    public <V> void write(V value, Class<V> valueType) {
        write(value, valueType, WebletWriteOptions.of());
    }

    @Override
    public <V> void write(V value, Class<V> valueType, Object attachment) {
        write(value, valueType, WebletWriteOptions.of(attachment));
    }

    @Override
    public <V> void write(V value, Class<V> valueType, WebletWriteOptions options) {

        boolean isWebletResponse = value instanceof WebletResponse;

        Object targetValue = isWebletResponse ? ((WebletResponse) value).unwrap() : value;
        Class<?> targetType = isWebletResponse ? targetValue.getClass() : valueType;

        WebletTeleWriter writer;
        if (options.writerClass() != null) {
            writer = factory.writer(options.writerClass());
        } else {
            writer = factory.writer(WebletTeleWriter.class, targetType);
        }

        writer.write(targetValue, targetType, options);
    }


}
