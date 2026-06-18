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
import colesico.framework.profile.Profile;
import colesico.framework.restlet.RestletException;
import colesico.framework.restlet.teleapi.RestletTeleWriter;
import colesico.framework.restlet.teleapi.response.RestletResponse;
import colesico.framework.restlet.teleapi.writer.*;
import colesico.framework.telehttp.writer.ExceptionWriter;
import colesico.framework.telehttp.writer.ProfileWriter;

import jakarta.inject.Singleton;

@Producer
@Produce(PlainTextWriter.class)
@Produce(value = RestletResponseWriter.class, keyType = RestletResponse.class, classed = RestletResponse.class)
@Produce(value = RestletExceptionWriter.class, keyType = RestletTeleWriter.class, classed = RestletException.class)
public class RestletWritersProducer {

    @Singleton
    @Classed(Exception.class)
    public RestletTeleWriter exceptionWriter(ExceptionWriter impl) {
        return RestletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Profile.class)
    public RestletTeleWriter profileWriter(ProfileWriter impl) {
        return RestletWriterProxy.of(impl);
    }

}
