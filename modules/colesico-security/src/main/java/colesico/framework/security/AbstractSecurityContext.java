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
import colesico.framework.ioc.scope.ThreadScope;

/**
 * Security kit default implementation.
 * Extend this class to customize principal control.
 */
abstract public class AbstractSecurityContext<P extends Principal<?>> implements SecurityContext<P> {

    protected final ThreadScope threadScope;

    public AbstractSecurityContext(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    /**
     * Read principle from source.
     * Override this method to fine-grained principle read control: check validity,
     * enrich with extra data from database, e.t.c.
     */
    abstract protected P read();

    /**
     * Writes principal to source.
     * Override this method to get more specific control.
     */
    abstract protected P write(P principle);


    @Override
    public P principal() {
        // Check thread cache at first
        PrincipalHolder holder = threadScope.get(PrincipalHolder.SCOPE_KEY);
        if (holder != null) {
            return (P) holder.principal();
        } else {
            // Create temporary empty principal holder
            // for possible subsequent recursive principal() invocations
            threadScope.put(PrincipalHolder.SCOPE_KEY, new PrincipalHolder(null));
        }

        // No principal in cache. Retrieve principal from source
        P principal = read();
        // Store principal to cache
        threadScope.put(PrincipalHolder.SCOPE_KEY, new PrincipalHolder(principal));

        return principal;
    }

    /*
    @Override
    public void setPrincipal(P principal) {
        principal = write(principal);
        threadScope.put(PrincipalHolder.SCOPE_KEY, new PrincipalHolder(principal));
    }
*/
    @Override
    public <T> T invokeAs(Invocable<T> invocable, P principal) {
        PrincipalHolder holder = threadScope.get(PrincipalHolder.SCOPE_KEY);
        threadScope.put(PrincipalHolder.SCOPE_KEY, new PrincipalHolder(principal));
        try {
            return invocable.invoke();
        } finally {
            threadScope.put(PrincipalHolder.SCOPE_KEY, holder);
        }
    }

    public record PrincipalHolder(Principal principal) {
        public static final Key<PrincipalHolder> SCOPE_KEY = new TypeKey<>(PrincipalHolder.class);
    }

}
