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

import colesico.framework.ioc.production.Supplier;
import colesico.framework.security.Identity;
import colesico.framework.security.IdentityContext;
import colesico.framework.security.authentication.*;
import colesico.framework.security.SecurityManager;
import colesico.framework.security.authentication.AuthenticationOutcome;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Default implementation of the {@link SecurityManager}.
 * <p>
 * This manager orchestrates the authentication process by coordinating between
 * transport-level authentications, the authentication registry, and lifecycle handlers.
 * It also manages the security challenge (Identity) within the current scope.
 */
public class SecurityManagerImpl implements SecurityManager {

    protected final IdentityContext identityContext;
    protected final AuthContext authContext;
    private final Supplier<Authentication<AuthenticationMessage, LogoutMessage>> authenticationFactory;

    public SecurityManagerImpl(IdentityContext identityContext,
                               AuthContext authContext,
                               Supplier<Authentication> authenticationFactory) {
        this.identityContext = identityContext;
        this.authContext = authContext;
        this.authenticationFactory = (Supplier) authenticationFactory;
    }

    protected <A extends AuthenticationMessage> AuthenticationOutcome invokeAuthenticate(Authentication<A, ?> auth, A message) {

        if (auth == null) {
            throw new SecurityException("Authentication flow is null");
        }

        var outcome = auth.authenticate(message);

        if (outcome == null) {
            throw new SecurityException("Null authentication flow outcome");
        }

        return switch (outcome) {
            case AuthenticationOutcome.Success success -> {
                var identity = success.identity();
                if (identity == null) {
                    throw new SecurityException("Null identity for success authentication");
                }
                identityContext.setIdentity(identity);
                yield success;
            }
            case AuthenticationOutcome.Stage stage -> stage;
            case AuthenticationOutcome.Failure failure -> failure;
            case AuthenticationOutcome.Skip skip -> skip;
        };
    }

    @Override
    public <A extends AuthenticationMessage> AuthenticationResult authenticate(Authentication<A, ?> auth, A message) {
        identityContext.clear();
        if (message == null) {
            throw new SecurityException("Authentication message is null");
        }
        var outcome = invokeAuthenticate(auth, message);
        return outcome.result();
    }

    /**
     * Orchestrates the authentication process across provided authentications and matching authenticators.
     */
    @Override
    public AuthenticationResult authenticate(Iterable<Authentication<?, ?>> authentications) {

        identityContext.clear();

        if (authentications == null || !authentications.iterator().hasNext()) {
            return AuthenticationResult.failure("No authentication flows");
        }

        for (var auth : authentications) {
            var outcome = invokeAuthenticate(auth, null);
            if (outcome instanceof AuthenticationOutcome.Skip) {
                continue;
            }
            return outcome.result();
        }

        return AuthenticationResult.failure("No meaningful authentication");
    }

    @Override
    public AuthenticationResult authenticate() {
        var authentications = authContext.authentications();
        return authenticate(authentications);
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

        var authClass = identity.claim(Identity.AUTHENTICATION_CLAIM, Authentication.class);

        if (authClass.isPresent()) {
            var authentication = authenticationFactory.get(authClass.get());
            authentication.logout(new LogoutMessage.Default(identity));
        }
    }

    @Override
    public void logout() {
        var o = identityContext.identity();
        o.ifPresent(identity -> {
            identityContext.clear();
            logout(identity);
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
