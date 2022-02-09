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

import colesico.framework.teleapi.TRWFactory;
import colesico.framework.weblet.WebletResponse;
import colesico.framework.weblet.teleapi.*;

import javax.inject.Singleton;
import java.lang.reflect.Type;

@Singleton
public class WebletDataPortImpl implements WebletDataPort {

    protected final TRWFactory trwFactory;

    public WebletDataPortImpl(TRWFactory trwFactory) {
        this.trwFactory = trwFactory;
    }


    @Override
    public <V> V read(Type valueType) {
        return read(WebletTRContext.of(valueType));
    }

    @Override
    public <V> V read(WebletTRContext context) {
        WebletTeleReader reader;
        if (context.getReaderClass() != null) {
            // Get specified reader
            reader = trwFactory.getReader(context.getReaderClass());
        } else {
            // Get reader by value type
            reader = trwFactory.findReader(WebletTeleReader.class, context.getValueType());
            if (reader == null) {
                // Get default reader
                reader = trwFactory.getReader(WebletTeleReader.class, Object.class);
            }
        }
        return (V) reader.read(context);
    }

    @Override
    public <V> void write(V value, Type valueType) {
        write(value, WebletTWContext.of(valueType));
    }

    @Override
    public <V> void write(V value, WebletTWContext context) {

        boolean isWebletResponse = context.getValueType().equals(WebletResponse.class);

        // Obtain writer
        WebletTeleWriter writer;
        if (context.getWriterClass() != null) {
            // Get specified reader
            writer = trwFactory.getWriter(context.getWriterClass());
        } else {
            Type responseType;
            if (isWebletResponse) {
                responseType = ((WebletResponse) value).unwrap().getClass();
            } else {
                responseType = context.getValueType();
            }
            // Get reader by response type
            writer = trwFactory.getWriter(WebletTeleWriter.class, responseType);
        }

        // Write value
        if (isWebletResponse) {
            writer.write(((WebletResponse) value).unwrap(), context);
        } else {
            writer.write(value, context);
        }
    }

}
