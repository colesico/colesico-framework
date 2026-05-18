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

import colesico.framework.security.authentication.*;
import colesico.framework.security.authorization.*;


import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * The central entry point for security services within the framework.
 * <p>
 * Provides methods for authenticating subjects, managing the current identity's lifecycle,
 * and performing identity-scoped operations (impersonation).
 * This manager coordinates with {@link IdentityContext} to associate an identity
 * with the current execution scope.
 */
public interface SecurityManager {

    /**
     * Attempts to authenticate a subject using the provided collection of {@link AuthenticationSource}s.
     * The first source that provides a valid {@link AuthenticationRequest} will be used for authentication.
     * On success, the resulting {@link Identity} is bound to the current {@link IdentityContext}.
     */
    AuthenticationResult login(Iterable<AuthenticationSource> sources);

    /**
     * Performs authentication using the sources currently bound to the {@link SourceContext}.
     * This is the standard way to trigger authentication in a scoped environment (e.g., during an HTTP request).
     */
    AuthenticationResult login();

    /**
     * Retrieves the current {@link Identity} from the active {@link IdentityContext}.
     * Returns an empty Optional if the subject is not authenticated.
     */
    Optional<Identity<?>> identity();

    /**
     * Checks whether the current subject is authenticated.
     */
    default boolean isAuthenticated() {
        return identity().isPresent();
    }

    /**
     * Executes the given task as the specified {@link Identity}.
     * Temporarily replaces the current identity in the context and restores it after the task completes.
     * This is useful for impersonation or system-level background tasks.
     */
    <T> T callAs(Callable<T> callable, Identity<?> identity);

    /**
     * Executes the given runnable as the specified {@link Identity}.
     *
     * @see #callAs(Callable, Identity)
     */
    default void runAs(Identity<?> identity, Runnable runnable) {
        callAs(() -> {
            runnable.run();
            return null;
        }, identity);
    }

    /**
     * Performs a logout for the specified {@link Identity}.
     * Triggers appropriate logout handlers and notifies associated sources.
     */
    void logout(Identity<?> identity);

    /**
     * Performs a logout for the current {@link Identity} and clears the security context.
     */
    void logout();

    /**
     * Returns the current {@link Identity} or throws an {@link IdentityRequiredException}
     * if the subject is not authenticated.
     * Use this method when an identity is strictly required for the subsequent logic.
     */
    default Identity<?> requireIdentity() {
        return identity().orElseThrow(IdentityRequiredException::new);
    }

    /**
     * Checks if the current subject has permission to access a specific resource using the provided {@link Authorizer}.
     */
    default <D, R> AuthorizationResult<D> hasPermission(Authorizer<R, D> authorizer, R resource) {
        AuthorizationRequest<R> request =
                new AuthorizationRequest.Default<>(identity().orElse(null), resource);

        return authorizer.authorize(request);
    }
}
