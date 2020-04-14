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
import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.restlet.RestletErrorResponse;
import colesico.framework.restlet.teleapi.RestletDataPort;
import colesico.framework.restlet.teleapi.RestletJsonConverter;
import colesico.framework.restlet.teleapi.RestletTeleReader;
import colesico.framework.restlet.teleapi.RestletTeleWriter;
import colesico.framework.teleapi.TeleReader;
import colesico.framework.teleapi.TeleWriter;
import colesico.framework.weblet.teleapi.WebletTDRContext;
import colesico.framework.weblet.teleapi.WebletTDWContext;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Singleton
public class RestletDataPortImpl implements RestletDataPort {

    protected final Ioc ioc;
    protected final Provider<HttpContext> httpContextProv;
    protected final RestletJsonConverter jsonConverter;

    public RestletDataPortImpl(Ioc ioc, Provider<HttpContext> httpContextProv, RestletJsonConverter jsonConverter) {
        this.ioc = ioc;
        this.httpContextProv = httpContextProv;
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
    public <V> V read(Type valueType, WebletTDRContext context) {
        // Try to get accurate reader
        final Supplier<RestletTeleReader> supplier
                = ioc.supplierOrNull(new ClassedKey<>(RestletTeleReader.class.getCanonicalName(), typeToClassName(valueType)));
        if (supplier != null) {
            final TeleReader<V, WebletTDRContext> reader = supplier.get(null);
            return reader.read(context);
        }

        // No accurate reader here so are reading data as json
        HttpContext httpContext = httpContextProv.get();
        try (InputStream is = httpContext.getRequest().getInputStream()) {
            return jsonConverter.fromJson(is, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <V> void write(Type valueType, V value, WebletTDWContext context) {
        final Supplier<RestletTeleWriter> supplier
                = ioc.supplierOrNull(new ClassedKey<>(RestletTeleWriter.class.getCanonicalName(), typeToClassName(valueType)));
        if (supplier != null) {
            final TeleWriter<V, WebletTDWContext> writer = supplier.get(null);
            writer.write(value, context);
            return;
        }

        HttpContext httpContext = httpContextProv.get();
        HttpResponse httpResponse = httpContext.getResponse();
        String json = jsonConverter.toJson(value);
        if (json == null) {
            httpResponse.sendText("", RESPONSE_CONTENT_TYPE, 204);
        } else {
            httpResponse.sendData(ByteBuffer.wrap(json.getBytes(StandardCharsets.UTF_8)), RESPONSE_CONTENT_TYPE, 200);
        }
    }

    @Override
    public void writeError(RestletErrorResponse response, int httpCode) {
        HttpContext context = httpContextProv.get();
        String json = jsonConverter.toJson(response);
        context.getResponse().sendText(json, RESPONSE_CONTENT_TYPE, httpCode);
    }
}
