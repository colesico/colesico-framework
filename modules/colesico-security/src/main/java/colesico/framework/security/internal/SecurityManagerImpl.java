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

    protected AuthenticationResult<?> doAuthenticate(AuthenticationRequest request, AuthenticationCallback callback) {

        if (callback == null) {
            throw new SecurityException("Authentication callback is null");

        }
        var authenticators = authRegistry.findAuthenticators(request);
        if (authenticators.isEmpty()) {
            throw new SecurityException("Appropriate authenticator not found for request '" + request + "'");
        }

        for (Authenticator authenticator : authenticators) {
            var result = authenticator.authenticate(request);
            if (result == null) {
                throw new SecurityException("Null authentication result");
            }

            switch (result) {
                case AuthenticationResult.Success success -> {
                    var identity = success.identity();
                    if (identity == null) {
                        throw new SecurityException("Null Identity for success authentication");
                    }
                    identityContext.setIdentity(identity);
                    callback.onSuccess(identity);
                    return success;
                }
                case AuthenticationResult.Stage<?> stage -> {
                    callback.onStage(stage.challenge());
                    return stage;
                }
                case AuthenticationResult.Failure failure -> {
                    callback.onFailure(request, failure.error());
                    return failure;
                }
                // Skip
                default -> {
                    // nop - proceed to the next authenticator
                }
            }
        }

        return AuthenticationResult.failure("No success authentication");
    }


    @Override
    public <R extends AuthenticationRequest> AuthenticationResult<?> authenticate(R request, AuthenticationCallback<R, ?> callback) {
        identityContext.clear();
        return doAuthenticate(request, callback);
    }

    /**
     * Orchestrates the authentication process across provided sources and matching authenticators.
     */
    @Override
    @SuppressWarnings("unchecked")
    public AuthenticationResult<?> authenticate(Iterable<? extends AuthenticationSource<?, ?>> sources) {

        identityContext.clear();

        for (AuthenticationSource source : sources) {
            final AuthenticationRequest request = source.request();
            if (request == null) {
                continue;
            }
            return doAuthenticate(request, source);
        }

        return AuthenticationResult.failure("No acceptable authentication source");
    }

    @Override
    public AuthenticationResult<?> authenticate() {
        var sources = sourceContext.sources();
        if (sources != null) {
            return authenticate(sources);
        }
        return AuthenticationResult.failure("No authentication sources");
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
    public void logout(Identity<?> identity) {
        if (identity == null) {
            throw new SecurityException("Identity is null");
        }

        authRegistry.findAuthenticator(identity)
                .ifPresent(a -> a.logout(identity));

        authRegistry.findCallback(identity)
                .ifPresent(s -> s.onLogout(identity));

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
