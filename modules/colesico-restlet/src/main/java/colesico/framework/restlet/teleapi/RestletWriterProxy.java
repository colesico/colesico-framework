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

import colesico.framework.teleapi.TeleWriter;

import java.util.function.Function;

/**
 * Proxy to use any writer as restlet writer
 *
 * @param <V> value type
 * @param <C> target writer context type
 */
public final class RestletWriterProxy<V, C> implements RestletTeleWriter<V> {

    private final TeleWriter<V, C> writer;
    private final Function<RestletTWContext, C> ctxConverter;

    public RestletWriterProxy(TeleWriter<V, C> writer, Function<RestletTWContext, C> ctxConverter) {
        this.writer = writer;
        this.ctxConverter = ctxConverter;
    }

    @Override
    public void write(V value, RestletTWContext context) {
        writer.write(value, ctxConverter.apply(context));
    }
}
