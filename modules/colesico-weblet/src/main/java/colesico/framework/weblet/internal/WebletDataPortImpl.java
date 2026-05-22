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

package colesico.framework.weblet.internal;

import colesico.framework.teleapi.dataport.TRWFactory;
import colesico.framework.weblet.response.WebletResponse;
import colesico.framework.weblet.teleapi.*;

import jakarta.inject.Singleton;

import java.lang.reflect.Type;

@Singleton
public class WebletDataPortImpl implements WebletDataPort {

    protected final TRWFactory trwFactory;

    public WebletDataPortImpl(TRWFactory trwFactory) {
        this.trwFactory = trwFactory;
    }

    @Override
    public <V> V read(Type valueType) {
        return read(WebletReadOptions.of(valueType));
    }

    @Override
    public <V> V read(WebletReadOptions query) {
        WebletTeleReader reader;
        if (query.readerClass() != null) {
            // Get specified reader
            reader = trwFactory.getReader(query.readerClass());
        } else {
            // Get reader by value type
            reader = trwFactory.findReader(WebletTeleReader.class, query.valueType());
            if (reader == null) {
                // Get default reader
                reader = trwFactory.getReader(WebletTeleReader.class, Object.class);
            }
        }
        return (V) reader.read(query);
    }

    @Override
    public <V, P> V read(Type valueType, P attachment) {
        return read(WebletReadOptions.of(valueType, attachment));
    }

    @Override
    public <V> void write(V value, Type valueType) {
        write(value, WebletWriteOptions.of(valueType));
    }

    @Override
    public <V> void write(V value, WebletWriteOptions options) {

        boolean isWebletResponse = options.valueType().equals(WebletResponse.class);

        // Obtain writer
        WebletTeleWriter writer;
        if (options.writerClass() != null) {
            // Get specified reader
            writer = trwFactory.getWriter(options.writerClass());
        } else {
            Type responseType;
            if (isWebletResponse) {
                responseType = ((WebletResponse) value).unwrap().getClass();
            } else {
                responseType = options.valueType();
            }
            // Get reader by response type
            writer = trwFactory.getWriter(WebletTeleWriter.class, responseType);
        }

        // Write value
        if (isWebletResponse) {
            writer.write(((WebletResponse) value).unwrap(), options);
        } else {
            writer.write(value, options);
        }
    }

    @Override
    public <V, P> void write(V value, Type valueType, P attachment) {
        write(value, WebletWriteOptions.of(valueType, attachment));
    }

}
