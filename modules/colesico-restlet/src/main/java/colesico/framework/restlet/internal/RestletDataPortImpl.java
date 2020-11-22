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

import colesico.framework.http.HttpContext;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.restlet.RestletError;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.restlet.teleapi.reader.ObjectReader;
import colesico.framework.restlet.teleapi.writer.ObjectWriter;
import colesico.framework.router.RouterContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class RestletDataPortImpl implements RestletDataPort {

    protected final Ioc ioc;
    protected final Provider<HttpContext> httpContextProv;
    protected final Provider<RouterContext> routerContextProv;

    protected final RestletJsonConverter jsonConverter;

    public RestletDataPortImpl(Ioc ioc, Provider<HttpContext> httpContextProv, Provider<RouterContext> routerContextProv, RestletJsonConverter jsonConverter) {
        this.ioc = ioc;
        this.httpContextProv = httpContextProv;
        this.routerContextProv = routerContextProv;
        this.jsonConverter = jsonConverter;
    }

    protected String typeToClassName(Type valueType) {
        if (valueType instanceof Class) {
            return ((Class) valueType).getCanonicalName();
        } else {
            return valueType.getTypeName();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V read(Type valueType, RestletTRContext context) {
        if (context == null) {
            context = new RestletTRContext();
        }

        // Store value type to context
        // so that type can be used by readers
        context.setValueType(valueType);

        RestletTeleReader<V> reader;

        if (context.getReaderClass() != null) {
            // Use specified reader
            reader = ioc.instance(context.getReaderClass());
        } else {
            // Use reader by param type
            reader = ioc.instanceOrNull(new ClassedKey<>(RestletTeleReader.class.getCanonicalName(), typeToClassName(valueType)), null);

            // No accurate reader here so are reading data as object
            if (reader == null) {
                reader = (RestletTeleReader<V>) ioc.instance(ObjectReader.class);
            }
        }
        return reader.read(context);
    }

    @Override
    public <V> void write(Type valueType, V value, RestletTWContext context) {
        if (context == null) {
            context = new RestletTWContext();
        }

        RestletTeleWriter<V> writer;

        if (context.getWriterClass() != null) {
            // Specified writer
            writer = ioc.instance(context.getWriterClass());
        } else {
            // By type writer
            writer = ioc.instanceOrNull(new ClassedKey<>(RestletTeleWriter.class.getCanonicalName(), typeToClassName(valueType)), null);
        }

        if (writer == null) {
            // Default object writer
            writer = (RestletTeleWriter<V>) ioc.instance(ObjectWriter.class);
        }

        writer.write(value, context);
    }

    @Override
    public <T extends Throwable> void writeError(final T throwable) {

        RestletTWContext context = new RestletTWContext();
        RestletTeleWriter<T> throwableWriter = ioc.instanceOrNull(new ClassedKey<>(RestletTeleWriter.class.getCanonicalName(), typeToClassName(throwable.getClass())), null);

        if (throwableWriter != null) {
            throwableWriter.write(throwable, context);
            return;
        }

        // If no specific writer try to get root exception
        // and determine writer for it
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause != null) {
            RestletTeleWriter rootCauseWriter = ioc.instanceOrNull(new ClassedKey<>(RestletTeleWriter.class.getCanonicalName(), typeToClassName(rootCause.getClass())), null);
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
        error.setDetails(getMessages(throwable));
        RestletTeleWriter objWriter = ioc.instance(ObjectWriter.class);
        context.setHttpCode(500);
        objWriter.write(error, context);

    }

    protected List<String> getMessages(Throwable ex) {
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
