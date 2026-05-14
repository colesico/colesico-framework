/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.security.internal;

import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.security.*;
import colesico.framework.security.SecurityManager;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationPeer;
import colesico.framework.security.authentication.AuthenticationListener;
import colesico.framework.security.authentication.AuthenticationManager;
import colesico.framework.security.assist.basicauth.BasicAuthRequest;
import colesico.framework.security.authorization.RequireIdentityAudit;

import jakarta.inject.Singleton;

@Producer
@Produce(RequireIdentityAudit.class)
@Produce(value = IdentityContextImpl.class, keyType = IdentityContext.class, scoped = Singleton.class)
@Produce(value = AuthenticationManagerImpl.class, keyType = AuthenticationManager.class, scoped = Singleton.class)
@Produce(value = SecurityManagerImpl.class, keyType = SecurityManager.class, scoped = Singleton.class)
public class SecurityProducer {

    /**
     * Current identity producer
     */
    public Identity identity(SecurityManager sm) {
        return sm.authenticate().getIdentity().orElse(null);
    }

    @Substitute(Substitution.STUB)
    @Singleton
    public AuthenticationListener authListener() {
        return new AuthenticationListener() {
        };
    }

    @Substitute(Substitution.STUB)
    @Singleton
    public AuthenticationPeer authContextReader() {
        return new AuthenticationPeer() {
            @Override
            public AuthenticationRequest request() {
                return  BasicAuthRequest.of(null);
            }

            @Override
            public <C> void proceed(C challenge) {

            }

            @Override
            public void login(Identity<?> identity) {

            }

            @Override
            public void logout(Identity<?> identity) {

            }
        };
    }
}
