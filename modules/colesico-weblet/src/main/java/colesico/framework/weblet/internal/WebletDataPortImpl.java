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

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.weblet.Weblet;
import colesico.framework.weblet.WebletResponse;
import colesico.framework.weblet.teleapi.*;
import colesico.framework.weblet.teleapi.WebletTeleReader;

import javax.inject.Singleton;
import java.lang.reflect.Type;

@Singleton
public class WebletDataPortImpl implements WebletDataPort {

    protected final Ioc ioc;

    public WebletDataPortImpl(Ioc ioc) {
        this.ioc = ioc;
    }

    protected String typeToClassName(Type valueType) {
        if (valueType instanceof Class) {
            return ((Class) valueType).getCanonicalName();
        } else {
            return valueType.getTypeName();
        }
    }

    @Override
    public <V> V read(Type valueType, WebletTRContext context) {
        if (context == null) {
            context = new WebletTRContext();
        }
        WebletTeleReader<V> reader;
        if (context.getReaderClass() != null) {
            reader = ioc.instance(context.getReaderClass());
        } else {
            reader = ioc.instance(new ClassedKey<>(WebletTeleReader.class.getCanonicalName(), typeToClassName(valueType)), null);
        }
        return reader.read(context);
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
            writer = ioc.instance(context.getWriterClass());
        } else {
            Type responseType;
            if (isWebletResponse) {
                responseType = ((WebletResponse) value).unwrap().getClass();
            } else {
                responseType = valueType;
            }
            writer = ioc.instance(new ClassedKey<>(WebletTeleWriter.class.getCanonicalName(), typeToClassName(responseType)), null);
        }

        // Write value
        if (isWebletResponse) {
            writer.write(((WebletResponse) value).unwrap(), context);
        } else {
            writer.write(value, context);
        }
    }

}
