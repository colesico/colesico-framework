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

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Represents a verified security subject.
 * <p>
 * An Identity is the final result of the authentication process. It encapsulates
 * the subject's unique identifier and a set of claims (attributes and permissions)
 * describing the entity's properties and authorities.
 * <p>
 * Framework provides default implementation {@link Identity.Default}
 */
public interface Identity<I> {

    String AUTHENTICATOR_CLAIM = "authenticator";

    /**
     * The unique string identifier of this identity (e.g., UUID, username, or numeric ID).
     */
    I id();

    /**
     * Usage example: Long userId = identity.id(Long::valueOf);
     */
    default <T> T id(Function<I, T> mapper) {
        return mapper.apply(id());
    }

    /**
     * Any identity data
     */
    Map<String, Object> claims();

    default <T> Optional<T> claim(String key, Class<T> type) {
        Object value = claims().get(key);
        return type.isInstance(value) ? Optional.of(type.cast(value)) : Optional.empty();
    }

    /**
     * Default identity implementation
     */
    record Default<I>(I id, Map<String, Object> claims) implements Identity<I> {
    }

}
