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
import colesico.framework.telehttp.writer.StringifyWriter;
import colesico.framework.telehttp.writer.ProfileWriter;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.weblet.response.*;
import colesico.framework.weblet.WebletTeleWriter;

import colesico.framework.weblet.writer.*;
import jakarta.inject.Singleton;

@Producer
@Produce(value = RedirectWriter.class, keyType = WebletTeleWriter.class, classed = RedirectResponse.class)
@Produce(value = ForwardWriter.class, keyType = WebletTeleWriter.class, classed = ForwardResponse.class)
@Produce(value = BinaryWriter.class, keyType = WebletTeleWriter.class, classed = BinaryResponse.class)
@Produce(value = HtmlResponseWriter.class, keyType = WebletTeleWriter.class, classed = HtmlResponse.class)
@Produce(value = TextResponseWriter.class, keyType = WebletTeleWriter.class, classed = TextResponse.class)
public class WebletWritersProducer {

    @Singleton
    @Classed(Exception.class)
    public WebletTeleWriter exceptionWriter(ExceptionWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Profile.class)
    public WebletTeleWriter profileWriter(ProfileWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(String.class)
    public WebletTeleWriter stringWriter(StringifyWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Long.class)
    public WebletTeleWriter longWriter(StringifyWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Integer.class)
    public WebletTeleWriter integerWriter(StringifyWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Short.class)
    public WebletTeleWriter shortWriter(StringifyWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Byte.class)
    public WebletTeleWriter byteWriter(StringifyWriter impl) {
        return WebletWriterProxy.of(impl);
    }

    @Singleton
    @Classed(Character.class)
    public WebletTeleWriter charWriter(StringifyWriter impl) {
        return WebletWriterProxy.of(impl);
    }

}
