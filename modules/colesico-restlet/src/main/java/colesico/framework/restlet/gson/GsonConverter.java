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

package colesico.framework.restlet.gson;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.restlet.JsonConverter;
import colesico.framework.restlet.RestletException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import jakarta.inject.Singleton;

import java.io.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * Default json converter for restlet
 */
@Singleton
public class GsonConverter implements JsonConverter {

    protected final Gson gson;

    public GsonConverter(Polysupplier<GsonOptionsPrototype> options) {
        GsonBuilder builder = new GsonBuilder();

        builder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE)
                .registerTypeAdapter(byte[].class, new Base64ByteArrayAdapter())
                .registerTypeAdapter(Date.class, new IsoDateAdapter())
                .registerTypeAdapter(LocalDate.class, new IsoLocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new IsoLocalDateTimeAdapter())
        ;

        for (var option : options) {
            builder = option.configure(builder);
        }

        this.gson = builder.create();
    }

    @Override
    public void serialize(Object value, Type baseType, Charset charset, OutputStream outputStream) {
        Objects.requireNonNull(baseType, "baseType cannot be null");
        Objects.requireNonNull(outputStream, "outputStream cannot be null");

        Charset actualCharset = (charset != null) ? charset : JSON_CHARSET;

        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, actualCharset);
             JsonWriter jsonWriter = gson.newJsonWriter(writer)) {

            gson.toJson(value, baseType, jsonWriter);

        } catch (Exception e) {
            throw new RestletException(e, 500);
        }
    }

    @Override
    public <T> T deserialize(InputStream inputStream, Charset charset, Type targetType) {
        Objects.requireNonNull(targetType, "targetType cannot be null");
        Objects.requireNonNull(inputStream, "inputStream cannot be null");

        Charset actualCharset = (charset != null) ? charset : JSON_CHARSET;

        try (InputStreamReader reader = new InputStreamReader(inputStream, actualCharset);
             JsonReader jsonReader = gson.newJsonReader(reader)) {

            return gson.fromJson(jsonReader, targetType);

        } catch (Exception e) {
            throw new RestletException(e, 500);
        }
    }

}
