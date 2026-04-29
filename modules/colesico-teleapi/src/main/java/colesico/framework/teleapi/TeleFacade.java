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

package colesico.framework.teleapi;

import jakarta.inject.Provider;

/**
 * Unified facade for tele-command invocations.
 *
 * @param <T> Target whose method will be invoked (usually a service)
 * @param <M> Tele-commands registry (references to target methods)
 */
abstract public class TeleFacade<T, M extends TeleCommands> {

    public static final String TELE_FACADE_SUFFIX = "TeleFacade";
    public static final String TARGET_PROV_FIELD = "targetProvider";
    public static final String COMMANDS_METHOD = "commands";

    /**
     * Target provider.
     * Target - this is an object whose method will be invoked.
     * Typically, this is a service object.
     */
    protected final Provider<T> targetProvider;

    public TeleFacade(Provider<T> targetProvider) {
        this.targetProvider = targetProvider;
    }

    /**
     * Tele-commands registry  implemented in this tele facade.
     * Registry entries is used to resolve target methods that are called with tele-api.
     * @see TeleController#resolve(Object) 
     */
    abstract public M commands();
}