/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.restlet.internal;

import colesico.framework.restlet.RestletError;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.restlet.teleapi.reader.ValueReader;
import colesico.framework.restlet.teleapi.writer.ObjectWriter;
import colesico.framework.teleapi.TRWFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class RestletDataPortImpl implements RestletDataPort {

    private final Logger logger = LoggerFactory.getLogger(RestletDataPort.class);
    private final TRWFactory trwFactory;


    public RestletDataPortImpl(TRWFactory trwFactory) {
        this.trwFactory = trwFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V read(Type valueType) {
        return read(RestletTRContext.of(valueType));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V read(RestletTRContext context) {

        RestletTeleReader<V> reader;

        if (context.getReaderClass() != null) {
            // Use specified reader
            reader = (RestletTeleReader<V>) trwFactory.getReader(context.getReaderClass());
        } else {
            // Use reader by param type
            reader = trwFactory.findReader(RestletTeleReader.class, context.getValueType());

            // No accurate reader here so are reading data as object
            if (reader == null) {
                reader = (RestletTeleReader<V>) trwFactory.getReader(ValueReader.class);
            }
        }
        return reader.read(context);
    }

    @Override
    public <V> void write(V value, Type valueType) {
        write(value, RestletTWContext.of(valueType));
    }

    @Override
    public <V> void write(V value, RestletTWContext context) {
        RestletTeleWriter<V> writer;

        if (context.getWriterClass() != null) {
            // Specified writer
            writer = trwFactory.getWriter(context.getWriterClass());
        } else {
            // By type writer
            writer = trwFactory.findWriter(RestletTeleWriter.class, context.getValueType());
        }

        if (writer == null) {
            // Default object writer
            writer = (RestletTeleWriter<V>) trwFactory.getWriter(ObjectWriter.class);
        }

        writer.write(value, context);
    }

    @Override
    public <T extends Throwable> void writeError(final T throwable) {

        RestletTWContext context = RestletTWContext.of(throwable.getClass());
        RestletTeleWriter<T> throwableWriter = trwFactory.findWriter(RestletTeleWriter.class, throwable.getClass());
        if (throwableWriter != null) {
            throwableWriter.write(throwable, context);
            return;
        }

        // If no specific writer try to get root exception
        // and determine writer for it
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause != null) {
            RestletTeleWriter rootCauseWriter = trwFactory.findWriter(RestletTeleWriter.class, rootCause.getClass());
            if (rootCauseWriter != null) {
                rootCauseWriter.write(rootCause, context);
                return;
            }
        }

        // No specific writer,
        // Perform default writing
        RestletError error = new RestletError();
        error.setErrorCode(throwable.getClass().getCanonicalName());
        error.setMessage(ExceptionUtils.getRootCauseMessage(throwable));
        error.setDetails(getErrorMessages(throwable));
        RestletTeleWriter objWriter = trwFactory.getWriter(ObjectWriter.class);
        context.setStatusCode(500);
        objWriter.write(error, context);

    }

    private List<String> getErrorMessages(Throwable ex) {
        Throwable e = ex;
        List<String> messages = new ArrayList<>();

        int depth = 0;
        while (e != null) {
            String message = e.getMessage();
            if (StringUtils.isBlank(message)) {
                message = "no message";
            }
            messages.add(e.getClass().getCanonicalName() + ": " + message);
            if (e.getCause() == e) {
                e = null;
            } else {
                if (depth++ < 16) {
                    e = e.getCause();
                } else {
                    e = null;
                }
            }
        }
        return messages;
    }

}
