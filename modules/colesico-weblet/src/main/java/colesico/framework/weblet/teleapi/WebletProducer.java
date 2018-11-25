/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.weblet.teleapi;

import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;
import colesico.framework.weblet.assist.CSRFProtector;
import colesico.framework.weblet.internal.AuthenticatorImpl;
import colesico.framework.weblet.internal.WebletDataPortImpl;
import colesico.framework.weblet.internal.WebletTeleDriverImpl;
import colesico.framework.weblet.teleapi.Authenticator;
import colesico.framework.weblet.teleapi.WebletDataPort;
import colesico.framework.weblet.teleapi.WebletTeleDriver;
import colesico.framework.weblet.teleapi.writer.PrincipalWriterConfig;
import colesico.framework.weblet.teleapi.writer.ProfileWriterConfig;

import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;

@Producer(Rank.RANK_MINOR)
@Produce(CSRFProtector.class)
@Produce(WebletDataPortImpl.class)
@Produce(WebletTeleDriverImpl.class)
@Produce(AuthenticatorImpl.class)
public class WebletProducer {

    @Singleton
    public WebletDataPort getWebletDataPort(WebletDataPortImpl impl) {
        return impl;
    }

    @Singleton
    public WebletTeleDriver getWebTeledriver(WebletTeleDriverImpl impl) {
        return impl;
    }

    @Singleton
    public Authenticator getAuthenticator(AuthenticatorImpl impl) {
        return impl;
    }

    // Default config
    @Singleton
    public PrincipalWriterConfig getPrincipalWriterConfig() {
        return new PrincipalWriterConfig() {
            @Override
            public byte[] getSignatureKey() {
                try {
                    return "0123456789ABCDEF".getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    // Default config
    @Singleton
    public ProfileWriterConfig getProfileWriterConfig() {
        return new ProfileWriterConfig() {
            @Override
            public int getCookieValidityDays() {
                return 365;
            }
        };
    }

}
