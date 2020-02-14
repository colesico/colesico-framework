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

package colesico.framework.asyncjob.gson;

import colesico.framework.asyncjob.PayloadConverter;
import colesico.framework.ioc.production.Polysupplier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

@Singleton
public class GsonPayloadConverter implements PayloadConverter {

    protected final Gson gson;

    public GsonPayloadConverter(Polysupplier<JobGsonOptionsPrototype> options) {
        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE);
        options.forEach(o -> o.applyOptions(builder), null);
        this.gson = builder.create();
    }
    @Override
    public Object toObject(Type payloadType, String payloadStr) {
        return gson.fromJson(payloadStr,payloadType);
    }

    @Override
    public String fromObject(Object payload) {
        return gson.toJson(payload);
    }
}
