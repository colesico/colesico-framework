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

import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.TeleFactory;
import colesico.framework.telehttp.HttpReadOptions;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.TeleHttpReader;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.response.DynamicResponse;
import colesico.framework.weblet.*;

import jakarta.inject.Singleton;

@Singleton
public class WebletDataPortImpl implements WebletDataPort {

    protected final TeleFactory teleFactory;

    public WebletDataPortImpl(TeleFactory teleFactory) {
        this.teleFactory = teleFactory;
    }

    @Override
    public <V> V read(Class<V> baseType, WebletReadOptions options) {

        if (options.readerClass() != null) {
            // Require specified reader
            WebletTeleReader<V> reader = (WebletTeleReader) teleFactory.reader(options.readerClass());
            return reader.read(baseType, options);
        }

        // Find reader by baseType
        TeleHttpReader<V, HttpReadOptions> reader = teleFactory.findReader(baseType, WebletTeleReader.class, TeleHttpReader.class);
        if (reader == null) {
            // Require object reader
            reader = teleFactory.reader(Object.class, WebletTeleReader.class, TeleHttpReader.class);
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

        Object targetValue;
        if (value instanceof DynamicResponse dr) {
            targetValue = dr.value();
        } else {
            targetValue = value;
        }

        if (options.writerClass() != null) {
            // Require specified writer
            WebletTeleWriter<Object> writer = (WebletTeleWriter) teleFactory.writer(options.writerClass());
            writer.write(targetValue, options);
            return;
        }

        // Find writer by the exact runtime class of the value
        TeleHttpWriter<Object, HttpWriteOptions> writer = teleFactory.writer(targetValue.getClass(), WebletTeleWriter.class, TeleHttpWriter.class);
        if (writer == null) {
            // Find by base type
            writer = teleFactory.findWriter(baseType, WebletTeleWriter.class, TeleHttpWriter.class);
            if (writer == null) {
                // Get common object writer
                writer = teleFactory.writer(Object.class, WebletTeleWriter.class, TeleHttpWriter.class);
            }
        }

        writer.write(targetValue, options);
    }

}
