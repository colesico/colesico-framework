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

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TRContext;
import colesico.framework.teleapi.TWContext;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Security kit default implementation.
 * Extend this class to customize principal control.
 */
public class DefaultSecurityKit implements SecurityKit {

    protected final ThreadScope threadScope;
    protected final Provider<DataPort> dataPortProv;

    @Inject
    public DefaultSecurityKit(ThreadScope threadScope, Provider<DataPort> dataPortProv) {
        this.threadScope = threadScope;
        this.dataPortProv = dataPortProv;
    }

    /**
     * Controls the principal read from the data port.
     * Override this method to get more specific control.
     * This method is used to fine grained control of principal: check validity, enrich with extra data, e.t.c.
     *
     * @return Valid principal or null
     */
    protected InputControlResult controlInputPrincipal(Principal principal) {
        return InputControlResult.accept(principal);
    }

    /**
     * Controls the principal before write to the data port.
     * Override this method to get more specific control.
     */
    protected Principal controlOutputPrincipal(Principal principal) {
        return principal;
    }

    @Override
    public <P extends Principal> P getPrincipal() {
        // Check thread cache at first
        PrincipalHolder holder = threadScope.get(PrincipalHolder.SCOPE_KEY);
        if (holder != null) {
            return (P) holder.principal();
        } else {
            // Create temporary empty principal holder
            // for possible subsequent recursive getPrincipal() invocations
            threadScope.put(PrincipalHolder.SCOPE_KEY, new PrincipalHolder(null));
        }

        // No principal in cache. Retrieve principal from data port

        DataPort<TRContext, TWContext> port = dataPortProv.get();
        Principal principal = port.read(Principal.class);

        // Control principal
        InputControlResult result = controlInputPrincipal(principal);
        principal = result.principal;

        // Update principal on client
        if (!result.accepted) {
            port.write(principal, Principal.class);
        }

        // Store principal to cache
        threadScope.put(PrincipalHolder.SCOPE_KEY, new PrincipalHolder(principal));

        return (P) principal;
    }

    @Override
    public void setPrincipal(Principal principal) {
        DataPort port = dataPortProv.get();
        principal = controlOutputPrincipal(principal);
        port.write(principal, Principal.class);
        threadScope.put(PrincipalHolder.SCOPE_KEY, new PrincipalHolder(principal));
    }

    @Override
    public <T> T invokeAs(Invocable<T> invocable, Principal principal) {
        PrincipalHolder holder = threadScope.get(PrincipalHolder.SCOPE_KEY);
        threadScope.put(PrincipalHolder.SCOPE_KEY, new PrincipalHolder(principal));
        try {
            return invocable.invoke();
        } finally {
            threadScope.put(PrincipalHolder.SCOPE_KEY, holder);
        }
    }

    public record InputControlResult(Principal principal, boolean accepted) {

        public static InputControlResult reset(Principal principal) {
            return new InputControlResult(principal, false);
        }

        public static InputControlResult accept(Principal principal) {
            return new InputControlResult(principal, true);
        }
    }

    public record PrincipalHolder(Principal principal) {
        public static final Key<PrincipalHolder> SCOPE_KEY = new TypeKey<>(PrincipalHolder.class);
    }

}
