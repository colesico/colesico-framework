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

package colesico.framework.restlet.teleapi;

import colesico.framework.teleapi.TeleReader;
import colesico.framework.weblet.teleapi.WebletTRContext;
import colesico.framework.weblet.teleapi.WebletTeleReader;

import java.util.function.Function;

/**
 * Proxy to use another readers as restlet reader
 */
public final class RestletReaderProxy<V, C> implements RestletTeleReader<V> {

    private final TeleReader<V, C> reader;
    private final Function<RestletTRContext, C> ctxConverter;

    public RestletReaderProxy(TeleReader<V, C> reader, Function<RestletTRContext, C> ctxConverter) {
        this.reader = reader;
        this.ctxConverter = ctxConverter;
    }

    @Override
    public V read(RestletTRContext context) {
        return reader.read(ctxConverter.apply(context));
    }

    public static final Function<RestletTRContext, WebletTRContext> webletCtxConverter = c -> new WebletTRContext(c.getName(), c.getOriginFacade());

    public static <V> RestletReaderProxy<V, WebletTRContext> of(WebletTeleReader reader) {
        return new RestletReaderProxy<>(reader, webletCtxConverter);
    }
}
