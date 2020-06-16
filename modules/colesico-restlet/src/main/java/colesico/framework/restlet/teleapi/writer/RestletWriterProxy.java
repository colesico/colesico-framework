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

package colesico.framework.restlet.teleapi.writer;

import colesico.framework.restlet.teleapi.RestletTWContext;
import colesico.framework.restlet.teleapi.RestletTeleWriter;
import colesico.framework.telehttp.HttpTWContext;
import colesico.framework.telehttp.HttpTeleWriter;

/**
 * Proxy to use any http writer as restlet writer
 *
 * @param <V> value type
 */
public final class RestletWriterProxy<V> extends RestletTeleWriter<V> {

    private final HttpTeleWriter<V, HttpTWContext> writer;

    private RestletWriterProxy(HttpTeleWriter<V, HttpTWContext> writer) {
        super(writer);
        this.writer = writer;
    }

    @Override
    public void write(V value, RestletTWContext context) {
        writer.write(value, context);
    }

    public static <V> RestletWriterProxy of(HttpTeleWriter<V, HttpTWContext> writer) {
        return new RestletWriterProxy(writer);
    }

}
