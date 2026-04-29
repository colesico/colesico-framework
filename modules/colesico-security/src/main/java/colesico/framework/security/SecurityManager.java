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

import colesico.framework.security.authorization.AuthorizationContext;
import colesico.framework.security.authorization.AuthorizationResult;
import colesico.framework.security.authorization.Authorizer;
import colesico.framework.security.authorization.DefaultAuthorizationContext;

import java.util.concurrent.Callable;

/**
 * Security manager to provide basic security service.
 * Manager can store/obtain principal from different sources,
 * associates current principal instance to the current thread.
 */
public interface SecurityManager<P extends Principal<?>> {

    /**
     * Authenticates the client using the provided credentials.
     * If authentication is successful, the method should associate
     * the resulting principal with the current thread and return it.
     * In case of failure, should return null
     */
    P authenticate(Credentials credentials);

    /**
     * Returns the valid principal associated with the current process for authenticated
     * client or null for an anonymous.
     * Method must retrieve the principal from any source (eg from the data port)
     * then validate, enrich (if needed) and cache it for a subsequent quick return
     * within the current thread.
     */
    P principal();

    default boolean isAuthenticated() {
        return principal() != null;
    }

    /**
     * Call given closure as specified principal
     */
    <T> T callAs(Callable<T> callable, P principal);

    default void runAs(P principal, Runnable runnable) {
        callAs(() -> {
            runnable.run();
            return null;
        }, principal);
    }

    /**
     *  Complete reset of current authentication.
     */
    void logout();

    /**
     * Checks the presence of active valid principal.
     * If not present - throws PrincipalRequiredException
     */
    default void requirePrincipal() {
        P principal = principal();
        if (principal == null) {
            throw new PrincipalRequiredException();
        }
    }

    default <D, R> AuthorizationResult<D> hasPermission(Authorizer<P, R, D> authorizer, R resource) {
        AuthorizationContext<P, R> context = new DefaultAuthorizationContext<>(principal(), resource);
        return authorizer.authorize(context);
    }

    /**
     * Marker interface for any credentials
     */
    interface Credentials {
    }

}
