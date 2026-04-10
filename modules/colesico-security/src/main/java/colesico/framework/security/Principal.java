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

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

/**
 * Principal interface.
 * Principal is used to identify a user. The specific implementation depends
 * on the needs of the application and has to be implemented in the application.
 * Framework provides default implementation {@link DefaultPrincipal}
 */
public interface Principal {

    /**
     * Thread scope key for caching principle
     */
    Key<Principal> SCOPE_KEY = new TypeKey<>(Principal.class);

    /**
     * Principal ID
     */
    String id();
}
