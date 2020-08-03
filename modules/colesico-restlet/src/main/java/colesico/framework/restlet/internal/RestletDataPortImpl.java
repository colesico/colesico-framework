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
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.key.TypeKey;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.restlet.RestletErrorResponse;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.restlet.teleapi.reader.ObjectReader;
import colesico.framework.restlet.teleapi.writer.JsonWriter;
import colesico.framework.restlet.teleapi.writer.ObjectWriter;
import colesico.framework.router.RouterContext;
import colesico.framework.teleapi.TeleReader;
import colesico.framework.teleapi.TeleWriter;
import colesico.framework.telehttp.Origin;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static colesico.framework.http.HttpMethod.*;

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
        context.setValueType(valueType);

        RestletTeleReader<V> reader = null;

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
    public void writeError(RestletErrorResponse response, int httpCode) {
        RestletTeleWriter writer = ioc.instance(ObjectWriter.class);
        RestletTWContext context = new RestletTWContext();
        context.setHttpCode(httpCode);
        writer.write(response, context);
    }
}
