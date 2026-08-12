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
import colesico.framework.security.authentication.AuthenticationSource;
import colesico.framework.security.authentication.AuthenticationCallback;
import colesico.framework.security.authentication.AuthenticatorOutcome;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Default implementation of the {@link SecurityManager}.
 * <p>
 * This manager orchestrates the authentication process by coordinating between
 * transport-level sources, the authentication registry, and lifecycle handlers.
 * It also manages the security challenge (Identity) within the current scope.
 */
public class SecurityManagerImpl implements SecurityManager {

    protected final IdentityContext identityContext;
    protected final AuthenticationSourceContext sourceContext;
    protected final AuthenticationRegistry authRegistry;

    public SecurityManagerImpl(IdentityContext identityContext, AuthenticationSourceContext sourceContext, AuthenticationRegistry authRegistry) {
        this.identityContext = identityContext;
        this.sourceContext = sourceContext;
        this.authRegistry = authRegistry;
    }

    protected AuthenticatorOutcome doAuthenticate(AuthenticationRequest request,
                                                  AuthenticationCallback<?, ?> callback) {

        if (callback == null) {
            throw new SecurityException("Authentication callback is null");
        }

        var authenticators = authRegistry.findAuthenticators(request);
        if (authenticators.isEmpty()) {
            throw new SecurityException("No appropriate authenticator for request '" + request + "'");
        }

        for (Authenticator authenticator : authenticators) {
            var outcome = authenticator.authenticate(request, callback);
            if (outcome == null) {
                throw new SecurityException("Null authentication result");
            }

            switch (outcome) {
                case AuthenticatorOutcome.Success success -> {
                    var identity = success.identity();
                    if (identity == null) {
                        throw new SecurityException("Null Identity for success authentication");
                    }
                    identityContext.setIdentity(identity);
                    return success;
                }
                case AuthenticatorOutcome.Failure failure -> {
                    return failure;
                }
                case AuthenticatorOutcome.Stage stage -> {
                    return stage;
                }
                // Skip
                default -> {
                    // nop - proceed to the next authenticator
                }
            }
        }

        return AuthenticatorOutcome.skip("No meaningful authentication");
    }

    @Override
    public AuthenticationResult authenticate(AuthenticationRequest request, AuthenticationCallback<?, ?> callback) {
        identityContext.clear();
        var outcome = doAuthenticate(request, callback);
        if (outcome instanceof AuthenticatorOutcome.Skip) {
            return AuthenticationResult.failure("No meaningful authentication");
        }
        return outcome.result();
    }

    /**
     * Orchestrates the authentication process across provided sources and matching authenticators.
     */
    @Override
    @SuppressWarnings("unchecked")
    public AuthenticationResult authenticate(Iterable<AuthenticationSource<?, ?>> sources) {

        if (sources == null || !sources.iterator().hasNext()) {
            return AuthenticationResult.failure("No authentication sources");
        }

        identityContext.clear();

        for (AuthenticationSource source : sources) {
            final AuthenticationRequest request = source.request();
            if (request == null) {
                continue;
            }

            var outcome = doAuthenticate(request, source.callback());
            if (outcome instanceof AuthenticatorOutcome.Skip) {
                continue;
            }
            return outcome.result();
        }

        return AuthenticationResult.failure("No meaningful authentication");
    }

    @Override
    public AuthenticationResult authenticate() {
        var sources = sourceContext.sources();
        return authenticate(sources);
    }

    @Override
    public Optional<Identity<?>> identity() {
        return identityContext.identity();
    }

    /**
     * Revokes the authenticated state for the specified identity.
     * <p>
     * Locates the original issuing authenticator and transport source associated
     * with the {@link Identity} to invalidate their respective sessions or tokens,
     * then triggers registered lifecycle handlers.
     *
     * @param identity the identity to log out.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void logout(Identity<?> identity) {
        if (identity == null) {
            throw new SecurityException("Identity is null");
        }

        var authenticator = authRegistry.findAuthenticator(identity);
        if (authenticator.isPresent()) {
            var callback = authRegistry.findCallback(identity);
            if (callback.isPresent()) {
                authenticator.get().logout(identity, callback.get());
            } else {
                authenticator.get().logout(identity, null);
            }
        } else {
            throw new SecurityException("Identity authenticator is not defined");
        }
    }

    @Override
    public void logout() {
        var identity = identityContext.identity();
        identity.ifPresent(iden -> {
            identityContext.clear();
            logout(iden);
        });
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
            identityContext.setIdentity(previous.orElse(null));
        }
    }

}
