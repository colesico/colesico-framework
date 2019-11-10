/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.security;

/**
 * @author Vladlen Larionov
 */
public interface SecurityKit {

    String REQUIRE_AUTHORITY_METHOD="requireAuthority";
    String REQUIRE_PRINCIPAL_METHOD="requirePrincipal";

    /**
     * Returns active valid principal if it present.
     *
     * @return null if principal is invalid or absent.
     */
    <P extends Principal> P getPrincipal();

    void setPrincipal(Principal principal);

    /**
     * Check active principal for at least one valid authority.
     *
     * @param authorityId
     * @return false if all are invalid
     */
    boolean hasAuthority(String... authorityId);

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

    /**
     * For the active principal checks the presence of at least one valid authority.
     * If there is not one - throws AuthorityRequiredException
     *
     * @param authorityId
     */
    default void requireAuthority(String... authorityId) {
        Principal principal = getPrincipal();
        if (principal == null) {
            throw new AuthorityRequiredException(authorityId);
        }

        if (!hasAuthority(authorityId)) {
            throw new AuthorityRequiredException(authorityId);
        }
    }
}
