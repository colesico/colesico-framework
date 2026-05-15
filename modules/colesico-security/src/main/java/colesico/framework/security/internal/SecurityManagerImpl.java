/*
 * Copyright © 2014-2026 Vladlen V. Larionov and others as noted.
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

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.key.Key;
import colesico.framework.security.Identity;
import colesico.framework.security.IdentityContext;
import colesico.framework.security.authentication.*;
import colesico.framework.security.SecurityManager;
import colesico.framework.security.authentication.AuthenticationPeer;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Security context default implementation.
 * Extend this class to customize identity control.
 */
public class SecurityManagerImpl implements SecurityManager {

    protected final Ioc ioc;

    protected final IdentityContext identityContext;
    protected final AuthenticationListener authenticationListener;
    protected final AuthenticationPeer authenticationPeer;

    public SecurityManagerImpl(Ioc ioc,
                               IdentityContext identityContext,

                               AuthenticationListener authenticationListener,
                               AuthenticationPeer authenticationPeer) {
        this.ioc = ioc;
        this.identityContext = identityContext;
        this.authenticationListener = authenticationListener;
        this.authenticationPeer = authenticationPeer;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Authenticator<AuthenticationRequest> authenticator(Class<? extends AuthenticationRequest> requestClass) {
        if (requestClass == null) {
            throw new SecurityException("Authentication request class is null");
        }
        Key<Authenticator> authIocKey = new ClassedKey<>(Authenticator.class, requestClass);
        Authenticator<AuthenticationRequest> authenticator = ioc.instanceOrNull(authIocKey);
        if (authenticator == null) {
            throw new SecurityException("Authenticator not found for request class '" + requestClass.getCanonicalName() + "'");
        }
        return authenticator;
    }

    private Authenticator<?> authenticator(Identity<?> identity) {
        @SuppressWarnings("unchecked")
        Class<? extends Authenticator<?>> authenticatorClass =
                (Class<? extends Authenticator<?>>) identity.claims().get(Identity.AUTHENTICATOR_CLAIM);

        if (authenticatorClass != null) {
            return ioc.instance(authenticatorClass);
        }
        return null;
    }

    @Override
    public AuthenticationResult authenticate(AuthenticationRequest request) {
        var authenticator = authenticator(request.getClass());
        var result = authenticator.login(request);
        result = authenticationListener.onAuthenticate(result, request);
        switch (result) {
            case AuthenticationResult.Success s -> {
                var identity = s.identity();
                if (identity == null) {
                    throw new SecurityException("Null Identity for success authentication");
                }
                identityContext.setIdentity(identity);
                authenticationPeer.login(identity);
            }
            case AuthenticationResult.Continuation<?> c -> {
                authenticationPeer.proceed(c.challenge());
            }

            default -> {
            }
        }

        return result;
    }

    @Override
    public AuthenticationResult authenticate() {
        AuthenticationRequest request = authenticationPeer.request();
        if (request != null) {
            return authenticate(request);
        }
        return AuthenticationResult.failure("Unauthenticated");
    }

    @Override
    public Optional<Identity<?>> identity() {
        return Optional.of(identityContext.identity());
    }

    @Override
    public void logout(Identity<?> identity) {
        var authenticator = authenticator(identity);
        if (authenticator != null) {
            authenticator.logout(identity);
            authenticationListener.onLogout(identity);
        }
    }

    @Override
    public void logout() {
        var identity = identityContext.identity();

        if (identity != null) {
            identityContext.clear();
            var authenticator = authenticator(identity);
            if (authenticator != null) {
                authenticator.logout(identity);
            }
            authenticationListener.onLogout(identity);
        }
    }

    @Override
    public <T> T callAs(Callable<T> callable, Identity<?> identity) {
        var previous = identityContext.identity();
        identityContext.setIdentity(identity);
        try {
            return callable.call();
        } catch (Exception e) {
            throw (e instanceof RuntimeException re) ? re : new RuntimeException(e);
        } finally {
            identityContext.setIdentity(previous);
        }
    }

}
