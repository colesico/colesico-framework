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

package colesico.framework.weblet.internal;

import colesico.framework.telehttp.writer.ExceptionWriter;
import colesico.framework.telehttp.writer.PlainTextWriter;
import colesico.framework.telehttp.writer.ProfileWriter;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.weblet.response.*;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.writer.*;

import jakarta.inject.Singleton;

@Producer
@Produce(StringWriter.class)
@Produce(RedirectWriter.class)
@Produce(ForwardWriter.class)
@Produce(BinaryWriter.class)
public class WebletWritersProducer {

    @Singleton
    @Classed(Exception.class)
    public WebletTeleWriter exceptionWriter(ExceptionWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(BinaryResponse.class)
    public WebletTeleWriter binaryWriter(BinaryWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(TextResponse.class)
    public WebletTeleWriter textResponseWriter(StringWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(HtmlResponse.class)
    public WebletTeleWriter htmlResponseWriter(StringWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(RedirectResponse.class)
    public WebletTeleWriter redirectResponseWriter(RedirectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(ForwardResponse.class)
    public WebletTeleWriter forwardResponseWriter(ForwardWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Profile.class)
    public WebletTeleWriter profileWriter(ProfileWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(String.class)
    public WebletTeleWriter stringWriter(PlainTextWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Long.class)
    public WebletTeleWriter longWriter(PlainTextWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Integer.class)
    public WebletTeleWriter integerWriter(PlainTextWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Short.class)
    public WebletTeleWriter shortWriter(PlainTextWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Byte.class)
    public WebletTeleWriter byteWriter(PlainTextWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Character.class)
    public WebletTeleWriter charWriter(PlainTextWriter impl) {
        return WebletWriterProxy.of(impl);
    }

}
