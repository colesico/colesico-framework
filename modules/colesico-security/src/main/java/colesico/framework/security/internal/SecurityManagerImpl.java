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

    protected final IdentityContext context;
    protected final AuthenticationListener listener;
    protected final AuthenticationPeer peer;
    protected final AuthenticationRegistry registry;

    public SecurityManagerImpl(IdentityContext context, AuthenticationListener listener, AuthenticationPeer peer, AuthenticationRegistry registry) {
        this.context = context;
        this.listener = listener;
        this.peer = peer;
        this.registry = registry;
    }

    @Override
    public AuthenticationResult login(AuthenticationRequest request) {
        var authenticator = registry.findAuthenticator(request);

        var result = authenticator.login(request);
        result = listener.onLogin(result, request);

        if (result instanceof AuthenticationResult.Success success) {
            var identity = success.identity();
            if (identity == null) {
                throw new SecurityException("Null Identity for success authentication");
            }
            context.setIdentity(identity);
            peer.authenticate(identity);
        } else if (result instanceof AuthenticationResult.Continuation<?> continuation) {
            peer.proceed(continuation.challenge());
        }

        return result;
    }

    @Override
    public AuthenticationResult login() {
        AuthenticationRequest request = peer.request();
        if (request != null) {
            return login(request);
        }
        return AuthenticationResult.failure("Unauthenticated");
    }

    @Override
    public Optional<Identity<?>> identity() {
        return Optional.ofNullable(context.identity());
    }

    @Override
    public void logout(Identity<?> identity) {
        if (identity == null) {
            return;
        }
        registry.findLogoutHandler(identity)
                .ifPresent(handler -> handler.logout(identity));
        listener.onLogout(identity);
    }

    @Override
    public void logout() {
        var identity = context.identity();
        if (identity != null) {
            context.clear();
            logout(identity);
        }
    }

    @Override
    public <T> T callAs(Callable<T> callable, Identity<?> identity) {
        final var previous = context.identity();
        context.setIdentity(identity);
        try {
            return callable.call();
        } catch (Exception e) {
            throw (e instanceof RuntimeException re) ? re : new RuntimeException(e);
        } finally {
            context.setIdentity(previous);
        }
    }

}
