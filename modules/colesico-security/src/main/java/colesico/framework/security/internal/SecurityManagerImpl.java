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

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.security.Identity;
import colesico.framework.security.IdentityContext;
import colesico.framework.security.authentication.*;
import colesico.framework.security.SecurityManager;
import colesico.framework.security.authentication.AuthenticationSource;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Default implementation of the {@link SecurityManager}.
 * <p>
 * This manager orchestrates the authentication process by coordinating between
 * transport-level sources, the authentication registry, and lifecycle handlers.
 * It also manages the security context (Identity) within the current scope.
 */
public class SecurityManagerImpl implements SecurityManager {

    protected final IdentityContext identityContext;
    protected final SourceContext sourceContext;
    protected final Polysupplier<AuthenticationHandler> authHandlers;
    protected final AuthenticationRegistry authRegistry;

    public SecurityManagerImpl(IdentityContext identityContext, SourceContext sourceContext, Polysupplier<AuthenticationHandler> authHandlers, AuthenticationRegistry authRegistry) {
        this.identityContext = identityContext;
        this.sourceContext = sourceContext;
        this.authHandlers = authHandlers;
        this.authRegistry = authRegistry;
    }

    protected AuthenticationResult handleLogin(Optional<AuthenticationRequest> request, AuthenticationResult result) {
        for (var h : authHandlers) {
            var r = h.handleLogin(request, result);
            result = r.result();
            if (!r.proceed()) {
                break;
            }
        }
        return result;
    }

    protected void handleLogout(Optional<Identity<?>> identity) {
        for (var h : authHandlers) {
            var r = h.handleLogout(identity);
            if (!r.proceed()) {
                break;
            }
        }
    }

    @Override
    public AuthenticationResult login(Iterable<AuthenticationSource> sources) {

        AuthenticationResult lastFailure = null;

        for (var source : sources) {
            final var request = source.request();
            if (request == null) {
                continue;
            }
            var authenticator = authRegistry.findAuthenticator(request).orElseThrow(
                    () -> new SecurityException("Appropriate authenticator not found for request '" + request + "'")
            );

            var result = authenticator.login(request);
            result = handleLogin(Optional.of(request), result);

            switch (result) {
                case AuthenticationResult.Success success -> {
                    var identity = success.identity();
                    if (identity == null) {
                        throw new SecurityException("Null Identity for success authentication");
                    }
                    identityContext.setIdentity(identity);
                    source.authenticate(identity);
                    return result;
                }
                case AuthenticationResult.Continuation<?> continuation -> {
                    source.proceed(continuation.challenge());
                    return result;
                }
                default -> {
                    source.unauthenticated(request);
                    lastFailure = result;
                }
            }

        } // for sources

        return lastFailure != null ? lastFailure :
                handleLogin(Optional.empty(), AuthenticationResult.failure("No acceptable authentication source"));
    }

    @Override
    public AuthenticationResult login() {
        var sources = sourceContext.sources();
        if (sources != null) {
            return login(sources);
        }
        return AuthenticationResult.failure("Unauthenticated");
    }

    @Override
    public Optional<Identity<?>> identity() {
        return Optional.ofNullable(identityContext.identity());
    }

    @Override
    public void logout(Identity<?> identity) {
        if (identity != null) {
            authRegistry.findAuthenticator(identity)
                    .ifPresent(a -> a.logout(identity));
            authRegistry.findAuthenticationSource(identity)
                    .ifPresent(s -> s.logout(identity));
            handleLogout(Optional.of(identity));
        } else {
            handleLogout(Optional.empty());
        }
    }

    @Override
    public void logout() {
        var identity = identityContext.identity();
        if (identity != null) {
            identityContext.clear();
            logout(identity);
        }
    }

    @Override
    public <T> T callAs(Callable<T> callable, Identity<?> identity) {
        final var previous = identityContext.identity();
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
