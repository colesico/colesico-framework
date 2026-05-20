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

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.NamedKey;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;
import colesico.framework.security.authentication.Authenticator;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Represents a verified security subject.
 * <p>
 * An Identity is the final result of the authentication process. It encapsulates
 * the subject's unique identifier and a set of claims (attributes and permissions)
 * describing the entity's properties and authorities.
 * <p>
 * The framework provides a default implementation: {@link Identity.Default}
 */
public interface Identity<I> {

    /**
     * Specifies {@link Authenticator} instance class that issued this identity
     * to retrieve instance from {@link Ioc} to route security actions, such as logout,
     * to the correct authenticator.
     */
    String AUTHENTICATOR_CLAIM = "authenticator";

    /**
     * @see AuthenticationRequest#SOURCE_CLAIM
     */
    String SOURCE_CLAIM = AuthenticationRequest.SOURCE_CLAIM;

    /**
     * The claim key for the roles holder.
     */
    String ROLES_CLAIM = "roles";

    /**
     * The claim key for the permissions holder.
     */
    String PERMISSIONS_CLAIM = "permissions";

    /**
     * Returns the unique identifier of this identity (e.g., UUID, login, or numeric ID).
     */
    I id();

    /**
     * Maps the identity identifier to another type.
     * Usage example: Long userId = identity.id(Long::valueOf);
     */
    default <T> T id(Function<I, T> mapper) {
        return mapper.apply(id());
    }

    /**
     * Returns a map of all claims associated with this identity.
     */
    Map<String, Object> claims();

    /**
     * Retrieves a claim by its key and casts it to the specified type.
     */
    default <T> Optional<T> claim(String key, Class<T> type) {
        Object value = claims().get(key);
        return type.isInstance(value) ? Optional.of(type.cast(value)) : Optional.empty();
    }

    /**
     * Syntactic sugar for retrieving a typed claim with a default value.
     */
    default <T> T claimOrElse(String key, Class<T> type, T defaultValue) {
        return claim(key, type).orElse(defaultValue);
    }

    /**
     * Provides built-in support for Roles (RBAC).
     */
    default Set<String> roles() {
        return claim(ROLES_CLAIM, Set.class).orElse(Collections.emptySet());
    }

    /**
     * Checks if the identity has the specified role.
     */
    default boolean hasRole(String role) {
        return roles().contains(role);
    }

    /**
     * Provides built-in support for Permissions/Authorities.
     */
    default Set<String> permissions() {
        return claim(PERMISSIONS_CLAIM, Set.class).orElse(Collections.emptySet());
    }

    /**
     * Checks if the identity has the specified permission.
     */
    default boolean hasPermission(String permission) {
        return permissions().contains(permission);
    }

    /**
     * The default implementation of the {@link Identity} interface.
     */
    record Default<I>(I id, Map<String, Object> claims) implements Identity<I> {
        public static <I> Default<I> of(I id) {
            return new Default<>(id, Map.of());
        }

        public static <I> Default<I> of(I id, Map<String, Object> claims) {
            return new Default<>(id, claims);
        }
    }
}