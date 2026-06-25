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
import colesico.framework.restlet.*;
import colesico.framework.restlet.response.ObjectResponse;
import colesico.framework.teleapi.dataport.TeleFactory;
import colesico.framework.telehttp.*;
import colesico.framework.telehttp.response.DynamicResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import colesico.framework.restlet.writer.ObjectResponseWriter;

import jakarta.inject.Singleton;

@Singleton
public class RestletDataPortImpl implements RestletDataPort {

    private final Logger logger = LoggerFactory.getLogger(RestletDataPort.class);
    private final TeleFactory teleFactory;

    public RestletDataPortImpl(TeleFactory teleFactory) {
        this.teleFactory = teleFactory;
    }

    @Override
    public <V> V read(Class<V> baseType, RestletReadOptions options) {

        if (options.readerClass() != null) {
            // Require specified reader
            RestletTeleReader<V> reader = (RestletTeleReader) teleFactory.reader(options.readerClass());
            return reader.read(baseType, options);
        }

        // Find reader by baseType
        TeleHttpReader<V, HttpReadOptions> reader = teleFactory.findReader(baseType, RestletTeleReader.class, TeleHttpReader.class);
        if (reader == null) {
            // No accurate reader here so are reading data as object - get object reader
            reader = teleFactory.reader(Object.class, RestletTeleReader.class, TeleHttpReader.class);
        }
        return reader.read(baseType, options);
    }

    @Override
    public <V> V read(Class<V> baseType) {
        return read(baseType, RestletReadOptions.of());
    }

    @Override
    public <V> V read(Class<V> baseType, Object attachment) {
        return read(baseType, RestletReadOptions.of(attachment));
    }

    @Override
    public <V> void write(V value, Class<V> baseType, RestletWriteOptions options) {

        Object targetValue;
        if (value instanceof DynamicResponse(Object val)) {
            targetValue = val;
        } else {
            targetValue = value;
        }

        // Check for a custom writer specified in options
        if (options.writerClass() != null) {
            RestletTeleWriter<Object> writer = (RestletTeleWriter) teleFactory.writer(options.writerClass());
            writer.write(targetValue, options);
            return;
        }

        // Find writer by the exact runtime class of the value
        TeleHttpWriter<Object, HttpWriteOptions> writer;
        if (targetValue instanceof Throwable t) {
            writer = findExceptionWriter(t);
        } else {
            writer = teleFactory.findWriter(targetValue.getClass(), RestletTeleWriter.class, TeleHttpWriter.class);
        }

        // Find by baseType
        if (writer == null) {
            writer = teleFactory.findWriter(baseType, RestletTeleWriter.class, TeleHttpWriter.class);
            // Final fallback to the default object writer
            if (writer == null) {
                writer = teleFactory.writer(Object.class, RestletTeleWriter.class, TeleHttpWriter.class);
            }
        }

        writer.write(targetValue, options);

    }

    @Override
    public <V> void write(V value, Class<V> baseType) {
        write(value, baseType, RestletWriteOptions.of());
    }

    @Override
    public <V> void write(V value, Class<V> baseType, Object attachment) {
        write(value, baseType, RestletWriteOptions.of(attachment));
    }

    protected TeleHttpWriter<Object, HttpWriteOptions> findExceptionWriter(final Throwable throwable) {
        TeleHttpWriter<Object, HttpWriteOptions> writer = teleFactory.findWriter(throwable.getClass(), RestletTeleWriter.class, TeleHttpWriter.class);
        if (writer != null) {
            return writer;
        }

        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause != throwable) {
            writer = teleFactory.findWriter(rootCause.getClass(), RestletTeleWriter.class, TeleHttpWriter.class);
            if (writer != null) {
                return writer;
            }
        }

        if (throwable instanceof TeleHttpException) {
            writer = teleFactory.findWriter(TeleHttpException.class, RestletTeleWriter.class, TeleHttpWriter.class);
            if (writer != null) {
                return writer;
            }
        }

        return teleFactory.findWriter(Exception.class, RestletTeleWriter.class, TeleHttpWriter.class);

    }
}
