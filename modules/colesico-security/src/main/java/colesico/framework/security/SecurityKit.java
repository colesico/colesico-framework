/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
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
 * Provides basic security service
 */
public interface SecurityKit {

    /**
     * Returns the valid principal associated with the current process if the principal is present or null if absent.
     * Method must retrieve the principal from any source (eg from the data port)
     * then validate, enrich (if needed) and cache it for a subsequent quick return within the current thread.
     * Can throws an exception in case the principal is inconsistent.
     */
    <P extends Principal> P getPrincipal();

    /**
     * Associate the principal with the current process.
     * The method can store the principal to any source (eg write it to the data port to
     * store it on remote client) in order to return it on subsequent requests.
     */
    void setPrincipal(Principal principal);

    /**
     * Invokes given closure as specified principal
     */
    <T> T invokeAs(Invocable<T> invocable, Principal principal);

    /**
     * Checks the presence of active valid principal.
     * If not present - throws PrincipalRequiredException
     */
    default void requirePrincipal() {
        Principal principal = getPrincipal();
        if (principal == null) {
            throw new PrincipalRequiredException();
        }
    }

    @FunctionalInterface
    interface Invocable<T> {
        T invoke();
    }

}
