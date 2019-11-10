/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.restlet.teleapi.gson;

import colesico.framework.ioc.Polysupplier;
import colesico.framework.restlet.teleapi.RestletJsonConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;
import java.io.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * Default json converter for restlet
 */
@Singleton
public class GsonConverter implements RestletJsonConverter {

    protected final Gson gson;

    public GsonConverter(Polysupplier<RestletGsonOptionsPrototype> options) {
        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE)
                .registerTypeHierarchyAdapter(byte[].class, new GsonByteArrayToBase64())
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTime())
        ;
        options.forEach(o -> o.applyOptions(builder), null);
        this.gson = builder.create();
    }

    @Override
    public <T> String toJson(T obj) {
        return gson.toJson(obj);
    }

    @Override
    public <T> T fromJson(Reader reader, Type valueType) {
        return gson.fromJson(reader, valueType);
    }
}
