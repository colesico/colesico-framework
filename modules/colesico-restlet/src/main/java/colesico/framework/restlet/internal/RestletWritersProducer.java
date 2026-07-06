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

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.restlet.RestletWriter;
import colesico.framework.restlet.writer.*;
import colesico.framework.telehttp.response.ExceptionResponse;
import colesico.framework.telehttp.response.ObjectResponse;
import jakarta.inject.Singleton;

@Producer
@Produce(JsonObjectResponseWriter.class)
@Produce(JsonExceptionResponseWriter.class)
@Produce(JsonObjectWriter.class)
public class RestletWritersProducer {

    @Singleton
    @Classed(ObjectResponse.class)
    public RestletWriter objectResponseWriter(JsonObjectResponseWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(ExceptionResponse.class)
    public RestletWriter exceptionResponseWriter(JsonExceptionResponseWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Object.class)
    public RestletWriter objectResponseWriter(JsonObjectWriter impl) {
        return impl;
    }

}
