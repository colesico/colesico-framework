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
import colesico.framework.security.authentication.AuthenticationPeer;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Default implementation of the {@link SecurityManager}.
 * <p>
 * This manager orchestrates the authentication process by coordinating between
 * transport-level peers, the authentication registry, and lifecycle handlers.
 * It also manages the security context (Identity) within the current scope.
 */
public class SecurityManagerImpl implements SecurityManager {

    protected final IdentityContext identityContext;
    protected final PeerContext peerContext;
    protected final Polysupplier<AuthenticationHandler> authHandlers;
    protected final AuthenticationRegistry authRegistry;

    public SecurityManagerImpl(IdentityContext identityContext, PeerContext peerContext, Polysupplier<AuthenticationHandler> authHandlers, AuthenticationRegistry authRegistry) {
        this.identityContext = identityContext;
        this.peerContext = peerContext;
        this.authHandlers = authHandlers;
        this.authRegistry = authRegistry;
    }

    protected AuthenticationResult handleLogin(Optional<AuthenticationRequest> request, AuthenticationResult result) {
        for (var h : authHandlers) {
            result = h.handleLogin(request, result);
        }
        return result;
    }

    protected void handleLogout(Identity<?> identity) {
        authHandlers.forEach(h -> h.handleLogout(identity));
    }

    private PeerRequest lookupPeerRequest(Iterable<AuthenticationPeer> peers) {
        for (var p : peers) {
            var request = p.request();
            if (request != null) {
                return new PeerRequest(request, p);
            }
        }
        return null;
    }

    @Override
    public AuthenticationResult login(Iterable<AuthenticationPeer> peers) {

        var peerRequest = lookupPeerRequest(peers);
        if (peerRequest == null) {
            return handleLogin(Optional.empty(), AuthenticationResult.failure("No acceptable authentication peer"));
        }

        var authenticator = authRegistry.findAuthenticator(peerRequest.request);
        var result = authenticator.login(peerRequest.request);

        result = handleLogin(Optional.of(peerRequest.request), result);

        if (result instanceof AuthenticationResult.Success success) {
            var identity = success.identity();
            if (identity == null) {
                throw new SecurityException("Null Identity for success authentication");
            }
            identityContext.setIdentity(identity);
            peerRequest.peer.authenticate(identity);
        } else if (result instanceof AuthenticationResult.Continuation<?> continuation) {
            peerRequest.peer.proceed(continuation.challenge());
        }

        return result;
    }

    @Override
    public AuthenticationResult login() {
        var peer = peerContext.peers();
        if (peer != null) {
            return login(peer);
        }
        return AuthenticationResult.failure("Unauthenticated");
    }

    @Override
    public Optional<Identity<?>> identity() {
        return Optional.ofNullable(identityContext.identity());
    }

    @Override
    public void logout(Identity<?> identity) {
        if (identity == null) {
            return;
        }
        authRegistry.findLogoutHandler(identity)
                .ifPresent(handler -> handler.logout(identity));
        handleLogout(identity);
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

    protected record PeerRequest(AuthenticationRequest request, AuthenticationPeer peer) {

    }

}
