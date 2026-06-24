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
import colesico.framework.telehttp.response.DynamicResponse;
import colesico.framework.weblet.teleapi.*;

import jakarta.inject.Singleton;

@Singleton
public class WebletDataPortImpl implements WebletDataPort {

    protected final TeleFactory teleFactory;

    public WebletDataPortImpl(TeleFactory teleFactory) {
        this.teleFactory = teleFactory;
    }

    @Override
    public <V> V read(Class<V> baseType, WebletReadOptions options) {
        WebletTeleReader<V> reader;
        if (options.readerClass() != null) {
            // Use specified reader
            reader = (WebletTeleReader<V>) teleFactory.reader(options.readerClass());
        } else {
            // Use reader by value type
            reader = teleFactory.findReader(WebletTeleReader.class, baseType);
            if (reader == null) {
                // Get default reader
                reader = teleFactory.reader(WebletTeleReader.class, Object.class);
            }
        }
        return reader.read(baseType, options);
    }

    @Override
    public <V> V read(Class<V> baseType) {
        return read(baseType, WebletReadOptions.of());
    }

    @Override
    public <V> V read(Class<V> baseType, Object attachment) {
        return read(baseType, WebletReadOptions.of(attachment));
    }

    @Override
    public <V> void write(V value, Class<V> baseType) {
        write(value, baseType, WebletWriteOptions.of());
    }

    @Override
    public <V> void write(V value, Class<V> baseType, Object attachment) {
        write(value, baseType, WebletWriteOptions.of(attachment));
    }

    @Override
    public <V> void write(V value, Class<V> baseType, WebletWriteOptions options) {

        boolean isDynamicResponse = value instanceof DynamicResponse;

        Object targetValue;
        WebletTeleWriter writer = null;

        if (options.writerClass() != null) {
            // Get options specified writer
            writer = teleFactory.writer(options.writerClass());
        }

        if (isDynamicResponse) {
            targetValue = ((DynamicResponse) value).unwrap();
            if (writer == null) {
                writer = teleFactory.writer(WebletTeleWriter.class, targetValue.getClass());
            }
        } else {
            targetValue = value;
            if (writer == null) {
                writer = teleFactory.findWriter(WebletTeleWriter.class, value.getClass());
            }
            if (writer == null) {
                writer = teleFactory.writer(WebletTeleWriter.class, baseType);
            }
        }

        writer.write(targetValue, options);
    }

}
