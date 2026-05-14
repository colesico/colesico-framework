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

import colesico.framework.security.authentication.AuthenticationContext;
import colesico.framework.security.authentication.AuthenticationStatus;
import colesico.framework.security.authorization.*;
import colesico.framework.security.authentication.AuthenticationPeer;


import java.util.concurrent.Callable;

/**
 * Security manager to provide basic security service.
 * Manager can store/obtain identity from different sources,
 * associates current identity instance to the current thread.
 */
public interface SecurityManager {

    /**
     * Authenticate caller by provided {@link AuthenticationContext} and on success bind {@link Identity} to {@link IdentityContext}.
     */
    AuthenticationStatus login(AuthenticationContext context);

    /**
     * The same as {@link SecurityManager#login(AuthenticationContext)}
     * but retrieve {@link AuthenticationContext} with {@link AuthenticationPeer#context()}
     */
    AuthenticationStatus identity();

    /**
     * Check peer is authenticated
     */
    default boolean isAuthenticated() {
        return identity() instanceof AuthenticationStatus.Success;
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
    void logout();

    /**
     * Checks the presence of active valid identity.
     * If not present - throws IdentityRequiredException
     */
    default void requireIdentity() {
        if (!identity().isSuccess()) {
            throw new IdentityRequiredException();
        }
    }

    default <D, R> AuthorizationResult<D> hasPermission(Authorizer<R, D> authorizer, R resource) {
        AuthorizationRequest<R> request =
                new AuthorizationRequest.Default<>(identity().getIdentity().orElse(null), resource);

        return authorizer.authorize(request);
    }

}
