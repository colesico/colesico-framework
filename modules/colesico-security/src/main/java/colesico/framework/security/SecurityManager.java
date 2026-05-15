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

package colesico.framework.security;

import colesico.framework.security.authentication.AuthenticationPeer;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authorization.*;


import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Security manager to provide basic security service.
 * Manager can store/obtain identity from different sources,
 * associates current identity instance to the current thread.
 */
public interface SecurityManager {

    /**
     * Authenticate peer by provided {@link AuthenticationRequest} and on success bind {@link Identity} to {@link IdentityContext}.
     */
    AuthenticationResult login(AuthenticationRequest request);

    /**
     * Authenticate peer by extracted {@link AuthenticationRequest} from the peer
     * by {@link AuthenticationPeer#request()}
     */
    AuthenticationResult login();

    /**
     * Retrieves the current {@link Identity} from the local context {@link IdentityContext}
     */
    Optional<Identity<?>> identity();

    /**
     * Check peer is authenticated
     */
    default boolean isAuthenticated() {
        return identity().isPresent();
    }

    /**
     * Call given closure as specified identity
     */
    <T> T callAs(Callable<T> callable, Identity<?> identity);

    default void runAs(Identity<?> identity, Runnable runnable) {
        callAs(() -> {
            runnable.run();
            return null;
        }, identity);
    }

    /**
     * Complete reset of current authentication.
     */
    void logout(Identity<?> identity);

    void logout();

    /**
     * Checks the presence of active valid identity.
     * If not present - throws IdentityRequiredException
     */
    default Identity<?> requireIdentity() {
        return identity().orElseThrow(IdentityRequiredException::new);
    }

    default <D, R> AuthorizationResult<D> hasPermission(Authorizer<R, D> authorizer, R resource) {
        AuthorizationRequest<R> request =
                new AuthorizationRequest.Default<>(identity().orElse(null), resource);

        return authorizer.authorize(request);
    }
}
