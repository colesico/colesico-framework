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

package colesico.framework.security.authentication;

import colesico.framework.security.Identity;
import colesico.framework.security.IdentityContext;
import colesico.framework.security.SecurityManager;

import java.lang.annotation.*;

/**
 * Configures authentication behavior for methods or classes.
 *
 * @author Vladlen V. Larionov
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface Authenticate {

    /**
     * Authentication names to be used.
     */
    String[] value();

    /**
     * Determines the execution strategy for authentication.
     */
    Strategy strategy() default Strategy.IF_NECESSARY;

    /**
     * Strategies defining how and when authentication is triggered.
     */
    enum Strategy {

        /**
         * Always performs authentication (call {@link SecurityManager#authenticate(AuthenticationSource)}).
         */
        STRICT,

        /**
         * Performs authentication only if an {@link Identity} is missing from the {@link IdentityContext}.
         */
        IF_NECESSARY,

        /**
         * Only registers sources in the {@link AuthenticationsContext} for manual authentication
         * by calling {@link SecurityManager#authenticate()} later within the business logic.
         */
        DEFERRED
    }
}