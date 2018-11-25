/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.restlet.teleapi.converter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class JacksonJsonConverter implements JsonConverter {
    protected static final Logger log = LoggerFactory.getLogger(JacksonJsonConverter.class);

    private final ObjectMapper mapper;

    @Inject
    public JacksonJsonConverter() {

        mapper = new ObjectMapper();
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
                .withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
                .withSetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        SimpleModule mod = new SimpleModule("Json cotleta partition");
        mod.addSerializer(LocalDate.class, new LocalDateConverter.Serializer());
        mod.addDeserializer(LocalDate.class, new LocalDateConverter.Deserializer());
        mapper.registerModule(mod);
    }

    @Override
    public String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }

    }

    @Override
    public <T> T fromJson(String json, Class<T> type) {
        if (type == null) {
            throw new RuntimeException("Target json class is null");
        }
        try {
            T obj;
            obj = mapper.readValue(json, type);
            return obj;
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public <T> T fromJson(InputStream is, Class<T> type) {
        if (type == null) {
            throw new RuntimeException("Target json class is null");
        }
        try {
            T obj;
            obj = mapper.readValue(is, type);
            return obj;
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
