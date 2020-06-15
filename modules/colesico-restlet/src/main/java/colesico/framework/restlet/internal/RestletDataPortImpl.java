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
import colesico.framework.ioc.production.Supplier;
import colesico.framework.restlet.RestletErrorResponse;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.router.RouterContext;
import colesico.framework.teleapi.TeleReader;
import colesico.framework.teleapi.TeleWriter;
import colesico.framework.weblet.Origin;
import colesico.framework.weblet.teleapi.WebletTRContext;
import colesico.framework.weblet.teleapi.WebletTWContext;
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
        // Try to get accurate reader
        final Supplier<RestletTeleReader> supplier
                = ioc.supplierOrNull(new ClassedKey<>(RestletTeleReader.class.getCanonicalName(), typeToClassName(valueType)));
        if (supplier != null) {
            final TeleReader<V, RestletTRContext> reader = supplier.get(null);
            return reader.read(context);
        }

        // No accurate reader here so are reading data as json
        HttpContext httpContext = httpContextProv.get();
        HttpMethod requestMethod = httpContext.getRequest().getRequestMethod();

        // Should the value be read from input stream?
        Origin origin = context.getOriginFacade().getOrigin();

        boolean useInputStream = (origin.equals(Origin.AUTO) || origin.equals(Origin.BODY))
                && (requestMethod.equals(HTTP_METHOD_POST) || requestMethod.equals(HTTP_METHOD_PUT) || requestMethod.equals(HTTP_METHOD_PATCH));

        if (useInputStream) {
            try (InputStream is = httpContext.getRequest().getInputStream()) {
                return jsonConverter.fromJson(is, valueType);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                String strValue = context.getString(routerContextProv.get(), httpContext.getRequest());
                if (StringUtils.isBlank(strValue)) {
                    return null;
                }
                return jsonConverter.fromJson(strValue, valueType);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public <V> void write(Type valueType, V value, RestletTWContext context) {
        final Supplier<RestletTeleWriter> supplier
                = ioc.supplierOrNull(new ClassedKey<>(RestletTeleWriter.class.getCanonicalName(), typeToClassName(valueType)));
        if (supplier != null) {
            final TeleWriter<V, RestletTWContext> writer = supplier.get(null);
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
