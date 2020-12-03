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

package colesico.framework.weblet.internal;

import colesico.framework.teleapi.TeleFactory;
import colesico.framework.weblet.WebletResponse;
import colesico.framework.weblet.teleapi.*;

import javax.inject.Singleton;
import java.lang.reflect.Type;

@Singleton
public class WebletDataPortImpl implements WebletDataPort {

    protected final TeleFactory teleFactory;

    public WebletDataPortImpl(TeleFactory teleFactory) {
        this.teleFactory = teleFactory;
    }

    @Override
    public <V> V read(Type valueType, WebletTRContext context) {
        if (context == null) {
            context = new WebletTRContext();
        }
        context.setValueType(valueType);
        WebletTeleReader reader;
        if (context.getReaderClass() != null) {
            // Get specified reader
            reader = teleFactory.getReader(context.getReaderClass());
        } else {
            // Get reader by value type
            reader = teleFactory.findReader(WebletTeleReader.class, valueType);
            if (reader == null) {
                // Get default reader
                reader = teleFactory.getReader(WebletTeleReader.class, Object.class);
            }
        }
        return (V) reader.read(context);
    }

    @Override
    public <V> void write(Type valueType, V value, WebletTWContext context) {
        if (context == null) {
            context = new WebletTWContext();
        }

        boolean isWebletResponse = valueType.equals(WebletResponse.class);

        // Obtain writer
        WebletTeleWriter writer;
        if (context.getWriterClass() != null) {
            // Get specified reader
            writer = teleFactory.getWriter(context.getWriterClass());
        } else {
            Type responseType;
            if (isWebletResponse) {
                responseType = ((WebletResponse) value).unwrap().getClass();
            } else {
                responseType = valueType;
            }
            // Get reader by response type
            writer = teleFactory.getWriter(WebletTeleWriter.class, responseType);
        }

        // Write value
        if (isWebletResponse) {
            writer.write(((WebletResponse) value).unwrap(), context);
        } else {
            writer.write(value, context);
        }
    }

}
