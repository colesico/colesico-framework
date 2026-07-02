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

import colesico.framework.teleapi.dataport.TeleFactory;
import colesico.framework.telehttp.*;
import colesico.framework.weblet.*;

import jakarta.inject.Singleton;

import java.lang.reflect.Type;

@Singleton
public class WebletDataPortImpl
        extends TeleHttpDataPort<WebletReadOptions, WebletWriteOptions>
        implements WebletDataPort {

    public WebletDataPortImpl(TeleFactory teleFactory) {
        super(teleFactory);
    }

    @Override
    protected Class<? extends TeleHttpReader> readerBaseClass() {
        return WebletTeleReader.class;
    }

    @Override
    protected Class<? extends TeleHttpWriter> writerBaseClass() {
        return WebletTeleWriter.class;
    }

    @Override
    public <V> V read(Type baseType, Object metadata) {
        return read(WebletReadOptions.builder(baseType).metadata(metadata).build());
    }

    @Override
    public <V> void write(V value, Type baseType, Object metadata) {
        write(value, WebletWriteOptions.builder(baseType).metadata(metadata).build());
    }
}
