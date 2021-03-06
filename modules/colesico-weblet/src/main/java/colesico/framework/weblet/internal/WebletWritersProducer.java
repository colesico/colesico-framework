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

import colesico.framework.telehttp.writer.PlainTextWriter;
import colesico.framework.telehttp.writer.PrincipalWriter;
import colesico.framework.telehttp.writer.ProfileWriter;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.security.Principal;
import colesico.framework.weblet.*;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.writer.*;

import javax.inject.Singleton;

@Producer
@Produce(StringWriter.class)
@Produce(RedirectWriter.class)
@Produce(ForwardWriter.class)
@Produce(BinaryWriter.class)
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
    @Classed(RedirectResponse.class)
    public WebletTeleWriter getRedirectResponseWriter(RedirectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(ForwardResponse.class)
    public WebletTeleWriter getForwardResponseWriter(ForwardWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Principal.class)
    public WebletTeleWriter getPrincipalWriter(PrincipalWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Profile.class)
    public WebletTeleWriter getProfileWriter(ProfileWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(String.class)
    public WebletTeleWriter getStringWriter(PlainTextWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Long.class)
    public WebletTeleWriter getLongWriter(PlainTextWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Integer.class)
    public WebletTeleWriter getIntegerWriter(PlainTextWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Short.class)
    public WebletTeleWriter getShortWriter(PlainTextWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Byte.class)
    public WebletTeleWriter getByteWriter(PlainTextWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Character.class)
    public WebletTeleWriter getCharWriter(PlainTextWriter impl) {
        return WebletWriterProxy.of(impl);
    }

}
