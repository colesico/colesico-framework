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

import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;
import jakarta.inject.Provider;

/**
 * Unified facade for tele-command invocations.
 *
 * @param <T> Target whose method will be invoked (usually a service)
 * @param <C> Tele-commands registry (references to target methods)
 */
abstract public class TeleFacade<T,
        R extends ReadOptions,
        W extends WriteOptions,
        C extends TeleFacade.Commands> {

    public static final String TELE_FACADE_SUFFIX = "TeleFacade";

    public static final String TARGET_PROV_FIELD = "targetProvider";
    public static final String DATA_PORT_PROV_FIELD = "dataPortProvider";

    public static final String COMMANDS_METHOD = "commands";

    /**
     * An object whose method will be invoked, typically, this is a service object.
     */
    protected final Provider<T> targetProvider;
    protected final Provider<DataPort<R, W>> dataPortProvider;

    @SuppressWarnings("unchecked")
    public TeleFacade(Provider<T> targetProvider, Provider<DataPort> dataPortProvider) {
        this.targetProvider = targetProvider;
        this.dataPortProvider = (Provider) dataPortProvider;
    }

    /**
     * Tele-commands registry  implemented in this tele facade.
     * Registry entries is used to resolve target methods that are called with tele-api.
     *
     * @see TeleController#resolve(TeleController.Criteria)
     */
    abstract public C commands();

    /**
     * Marker interface
     */
    public interface Commands {
    }
}