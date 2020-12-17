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

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Objects;

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

    protected boolean isInputControlRequired(Principal principal) {
        return true;
    }

    /**
     * Controls the principal read from the data port.
     * Override this method to get more specific control.
     * This method is used to fine grained control of principal: check validity, enrich with extra data, e.t.c.
     *
     * @return Valid principal or null
     */
    protected Principal controlInputPrincipal(Principal principal) {
        return principal;
    }

    protected boolean isOutputControlRequired(Principal principal) {
        return true;
    }

    /**
     * Controls the principal before write to the data port.
     * Override this method to get more specific control.
     */
    protected Principal controlOutputPrincipal(Principal principal) {
        return principal;
    }


    @Override
    public final <P extends Principal> P getPrincipal() {
        // Check thread cache at first
        PrincipalHolder holder = threadScope.get(PrincipalHolder.SCOPE_KEY);
        if (holder != null) {
            return (P) holder.getPrincipal();
        } else {
            // Create temporary empty principal holder
            // for possible subsequent recursive getPrincipal() invocations
            threadScope.put(PrincipalHolder.SCOPE_KEY, new PrincipalHolder(null));
        }

        // No principal in cache. Retrieve principal from data port

        DataPort<Object, Object> port = dataPortProv.get();
        Principal principal = port.read(Principal.class, null);

        // Is control needed?
        if ((principal != null) && isInputControlRequired(principal)) {
            Principal p = controlInputPrincipal(principal);
            if (!Objects.equals(principal, p)) {
                port.write(Principal.class, p, null);
            }
            principal = p;
        }

        // Store principal to cache
        threadScope.put(PrincipalHolder.SCOPE_KEY, new PrincipalHolder(principal));

        return (P) principal;
    }

    @Override
    public final void setPrincipal(Principal principal) {
        DataPort port = dataPortProv.get();
        if ((principal != null) && isOutputControlRequired(principal)) {
            principal = controlOutputPrincipal(principal);
        }

        port.write(Principal.class, principal, null);
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

    public static final class PrincipalHolder {
        public static final Key<PrincipalHolder> SCOPE_KEY = new TypeKey<>(PrincipalHolder.class);
        private final Principal principal;

        public PrincipalHolder(Principal principal) {
            this.principal = principal;
        }

        public Principal getPrincipal() {
            return principal;
        }
    }

}
