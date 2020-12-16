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
     * Override this method to get more specific principal read control
     * This method is used to fine grained control of user principal: check validity, enrich with extra data, e.t.c.
     *
     * @return Valid principal or null
     */
    protected Principal principalReadControl(DataPort<Object, Object> port) {
        return port.read(Principal.class, null);
    }

    /**
     * Override this method to get more specific principal write control.
     * This method is used to fine grain write control of user principal.
     */
    protected void principalWriteControl(DataPort<Object, Object> port, Principal principal) {
        port.write(Principal.class, principal, null);
    }

    @Override
    public final <P extends Principal> P getPrincipal() {
        // Check thread cache at first
        PrincipalHolder holder = threadScope.get(PrincipalHolder.SCOPE_KEY);
        if (holder != null) {
            return (P) holder.getPrincipal();
        }

        // No principal in cache. Retrieve principal from client
        DataPort<Object, Object> port = dataPortProv.get();

        Principal principal = principalReadControl(port);

        // Store principal to cache and return
        threadScope.put(PrincipalHolder.SCOPE_KEY, new PrincipalHolder(principal));

        return (P) principal;
    }

    @Override
    public final void setPrincipal(Principal principal) {
        DataPort port = dataPortProv.get();
        principalWriteControl(port, principal);
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
