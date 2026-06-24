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

package colesico.framework.restlet.writer;

import colesico.framework.restlet.RestletWriteOptions;
import colesico.framework.restlet.RestletTeleWriter;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;

/**
 * Proxy to use any http writer as restlet writer
 *
 * @param <V> value type
 */
public final class RestletWriterProxy<V> implements RestletTeleWriter<V> {

    private final TeleHttpWriter<V, HttpWriteOptions> writer;

    public RestletWriterProxy(TeleHttpWriter<V, HttpWriteOptions> writer) {
        this.writer = writer;
    }

    @Override
    public void write(V value, Class<V> baseType, RestletWriteOptions options) {
        writer.write(value, baseType, options);
    }

    public static <V> RestletWriterProxy<V> of(TeleHttpWriter<V, HttpWriteOptions> writer) {
        return new RestletWriterProxy<>(writer);
    }

}
