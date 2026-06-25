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

@Singleton
public class RestletDataPortImpl
        extends TeleHttpDataPort<RestletReadOptions, RestletWriteOptions>
        implements RestletDataPort {

    public RestletDataPortImpl(TeleFactory teleFactory) {
        super(teleFactory);
    }

    @Override
    protected Class<? extends TeleHttpReader> readerBaseClass() {
        return RestletTeleReader.class;
    }

    @Override
    protected Class<? extends TeleHttpWriter> writerBaseClass() {
        return RestletTeleWriter.class;
    }

    @Override
    protected RestletReadOptions readOptions() {
        return RestletReadOptions.of();
    }

    @Override
    protected RestletReadOptions readOptions(Object attachment) {
        return RestletReadOptions.of(attachment);
    }

    @Override
    protected RestletWriteOptions writeOptions() {
        return RestletWriteOptions.of();
    }

    @Override
    protected RestletWriteOptions writeOptions(Object attachment) {
        return RestletWriteOptions.of(attachment);
    }
}
