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

package colesico.framework.restlet.teleapi.reader;

import colesico.framework.restlet.teleapi.RestletReadOptions;
import colesico.framework.restlet.teleapi.RestletTeleReader;
import colesico.framework.telehttp.HttpReadOptions;
import colesico.framework.telehttp.HttpTeleReader;

/**
 * Proxy to  use any http readers as restlet reader
 */
public final class RestletReaderProxy<V> implements RestletTeleReader<V> {

    private final HttpTeleReader<V, HttpReadOptions> reader;

    private RestletReaderProxy(HttpTeleReader<V, HttpReadOptions> reader) {
        this.reader = reader;
    }

    @Override
    public V read(Class<V> baseType, RestletReadOptions options) {
        return reader.read(baseType, options);
    }

    public static <V> RestletReaderProxy<V> of(HttpTeleReader<V, HttpReadOptions> reader) {
        return new RestletReaderProxy<>(reader);
    }

    @Override
    public String toString() {
        return "RestletReaderProxy{" +
                "reader=" + reader +
                '}';
    }

}
