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

/**
 * Security context to provide basic security service.
 * Context can store/obtain principal from different sources.
 * Context associates current principal instance to the current thread.
 */
public interface SecurityContext<P extends Principal> {

    /**
     * Returns the valid principal associated with the current process if the principal is present or null if absent.
     * Method must retrieve the principal from any source (eg from the data port)
     * then validate, enrich (if needed) and cache it for a subsequent quick return within the current thread.
     * Can throw an exception in case the principal is inconsistent.
     */
    P principal();

    /**
     * Invokes given closure as specified principal
     */
    <T> T invokeAs(Invocable<T> invocable, P principal);

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

    @FunctionalInterface
    interface Invocable<T> {
        T invoke();
    }

}
