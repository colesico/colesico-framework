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

package colesico.framework.restlet.internal;

import colesico.framework.assist.ExceptionUtils;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.restlet.teleapi.reader.ObjectReader;
import colesico.framework.restlet.teleapi.writer.RestletResponseWriter;
import colesico.framework.teleapi.dataport.TeleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;

@Singleton
public class RestletDataPortImpl implements RestletDataPort {

    private final Logger logger = LoggerFactory.getLogger(RestletDataPort.class);
    private final TeleFactory teleFactory;


    public RestletDataPortImpl(TeleFactory teleFactory) {
        this.teleFactory = teleFactory;
    }


    protected RestletTeleWriter findRootErrorWriter(final Throwable throwable) {
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause != null) {
            return teleFactory.findWriter(RestletTeleWriter.class, rootCause.getClass());
        }
        return null;
    }


    @Override
    public <V> V read(Class<V> valueType, RestletReadOptions options) {
        RestletTeleReader<V> reader;

        if (options.readerClass() != null) {
            // Use specified reader
            reader = (RestletTeleReader<V>) teleFactory.reader(options.readerClass());
        } else {
            // Use reader by value type
            reader = teleFactory.findReader(RestletTeleReader.class, valueType);
            if (reader == null) {
                // No accurate reader here so are reading data as object
                reader = (RestletTeleReader<V>) teleFactory.reader(ObjectReader.class);
            }
        }
        return reader.read(valueType, options);
    }

    @Override
    public <V> V read(Class<V> valueType) {
        return read(valueType, RestletReadOptions.of());
    }

    @Override
    public <V> V read(Class<V> valueType, Object attachment) {
        return read(valueType, RestletReadOptions.of(attachment));
    }

    @Override
    public <V> void write(V value, Class<V> valueType, RestletWriteOptions options) {
        RestletTeleWriter writer = null;

        // Check for a custom writer specified in options
        if (options.writerClass() != null) {
            writer = teleFactory.writer(options.writerClass());
        } else {

            // Find writer by the exact runtime class of the value
            writer = teleFactory.findWriter(RestletTeleWriter.class, value.getClass());

            // Handle specific logic for exceptions
            if (writer == null && value instanceof Throwable t) {
                writer = findRootErrorWriter(t);
            }

            // Fallback to the declared value type
            if (writer == null) {
                writer = teleFactory.findWriter(RestletTeleWriter.class, valueType);
            }

            // Final fallback to the default object writer
            if (writer == null) {
                writer = teleFactory.writer(RestletResponseWriter.class);
            }
        }

        writer.write(value, valueType, options);

    }

    @Override
    public <V> void write(V value, Class<V> valueType) {
        write(value, valueType, RestletWriteOptions.of());
    }

    @Override
    public <V> void write(V value, Class<V> valueType, Object attachment) {
        write(value, valueType, RestletWriteOptions.of(attachment));
    }
}
