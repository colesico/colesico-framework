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

package colesico.framework.restlet.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.restlet.teleapi.RestletTeleWriter;
import colesico.framework.restlet.teleapi.RestletWriterProxy;
import colesico.framework.security.Principal;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.NavigationResponse;
import colesico.framework.weblet.TextResponse;
import colesico.framework.weblet.VariedResponse;
import colesico.framework.weblet.teleapi.writer.*;

import javax.inject.Singleton;

@Producer
public class RestletWritersProducer {

    @Singleton
    @Classed(TextResponse.class)
    public RestletTeleWriter getTextResponseWriter(StringWriter impl) {
        return RestletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(HtmlResponse.class)
    public RestletTeleWriter getHtmlResponseWriter(StringWriter impl) {
        return RestletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(NavigationResponse.class)
    public RestletTeleWriter getNavigationResponseWriter(NavigationWriter impl) {
        return RestletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(VariedResponse.class)
    public RestletTeleWriter getVariedResponseWriter(VariedWriter impl) {
        return RestletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Principal.class)
    public RestletTeleWriter getPrincipalWriter(PrincipalWriter impl) {
        return RestletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Profile.class)
    public RestletTeleWriter getProfileWriter(ProfileWriter impl) {
        return RestletWriterProxy.of(impl);
    }
}
