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

import colesico.framework.ioc.message.IocMessage;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.restlet.RestletException;
import colesico.framework.restlet.teleapi.RestletTeleWriter;
import colesico.framework.restlet.teleapi.response.RestletResponse;
import colesico.framework.restlet.teleapi.writer.*;
import colesico.framework.telehttp.writer.ProfileWriter;

import jakarta.inject.Singleton;

@Producer
@Produce(RestletExceptionWriter.class)
@Produce(PlainTextWriter.class)
@Produce(RestletResponseWriter.class)
@Produce(PrincipalRequiredExceptionWriter.class)
public class RestletWritersProducer {

    @Singleton
    @Classed(RestletResponse.class)
    public RestletTeleWriter restletResponseWriter(@IocMessage String contentType, RestletResponseWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Profile.class)
    public RestletTeleWriter profileWriter(ProfileWriter impl) {
        return RestletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(RestletException.class)
    public RestletTeleWriter restletExceptionWriter(RestletExceptionWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(PrincipalRequiredException.class)
    public RestletTeleWriter principalRequiredExceptionWriter(PrincipalRequiredExceptionWriter impl) {
        return impl;
    }

}
