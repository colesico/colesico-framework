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

package colesico.framework.weblet.internal;

import colesico.framework.ioc.annotation.Classed;
import colesico.framework.ioc.annotation.Produce;
import colesico.framework.ioc.annotation.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.security.Principal;
import colesico.framework.weblet.*;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.writer.*;

import javax.inject.Singleton;



@Producer(MinorTag.class)
@Produce(StringWriter.class)
@Produce(NavigationWriter.class)
@Produce(BinaryWriter.class)
@Produce(VariedWriter.class)
@Produce(PrincipalWriter.class)
@Produce(ProfileWriter.class)
public class WebletWritersProducer {

    @Singleton
    @Classed(BinaryResponse.class)
    public WebletTeleWriter getBinaryWriter(BinaryWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(TextResponse.class)
    public WebletTeleWriter getTextResponseWriter(StringWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(HtmlResponse.class)
    public WebletTeleWriter getHtmlResponseWriter(StringWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(NavigationResponse.class)
    public WebletTeleWriter getNavigationResponseWriter(NavigationWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(VariedResponse.class)
    public WebletTeleWriter getVariedResponseWriter(VariedWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Principal.class)
    public WebletTeleWriter getPrincipalWriter(PrincipalWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Profile.class)
    public WebletTeleWriter getProfileWriter(ProfileWriter impl) {
        return impl;
    }

}
