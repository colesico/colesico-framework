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

package colesico.framework.restlet.internal;

import colesico.framework.restlet.*;
import colesico.framework.teleapi.dataport.TeleFactory;
import colesico.framework.telehttp.*;

import jakarta.inject.Singleton;

import java.lang.reflect.Type;

@Singleton
public class RestletDataPortImpl
        extends HttpDataPort<RestletReadOptions, RestletWriteOptions>
        implements RestletDataPort {

    public RestletDataPortImpl(TeleFactory teleFactory) {
        super(teleFactory);
    }

    @Override
    protected Class<? extends HttpReader> readerBaseClass() {
        return RestletReader.class;
    }

    @Override
    protected Class<? extends HttpWriter> writerBaseClass() {
        return RestletWriter.class;
    }

    @Override
    public <V> V read(Type baseType, Object metadata) {
        return read(RestletReadOptions.builder(baseType).metadata(metadata).build());
    }

    @Override
    public <V> void write(V value, Type baseType, Object metadata) {
        write(value, RestletWriteOptions.builder(baseType).metadata(metadata).build());
    }
}
