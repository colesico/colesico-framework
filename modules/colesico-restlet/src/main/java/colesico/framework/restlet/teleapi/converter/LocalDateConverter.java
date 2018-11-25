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


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Vladlen Larionov
 */
public class LocalDateConverter {

    protected static final DateTimeFormatter isoDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static class Deserializer extends JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            String val = jsonParser.readValueAs(String.class);
            if (StringUtils.isEmpty(val)){
                return null;
            }
            if (StringUtils.contains(val, "T")) {
                String[] vals = StringUtils.split(val, "T");
                return LocalDate.parse(vals[0], isoDateFormatter);
            } else {
                return LocalDate.parse(val, isoDateFormatter);
            }
        }
    }

    public static class Serializer extends JsonSerializer<LocalDate> {

        @Override
        public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (localDate == null) {
                return;
            }
            String dateStr = localDate.format(isoDateFormatter);
            jsonGenerator.writeString(dateStr);
        }
    }

}
