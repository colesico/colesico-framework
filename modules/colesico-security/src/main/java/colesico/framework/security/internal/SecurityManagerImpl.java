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

package colesico.framework.security.internal;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.security.Identity;
import colesico.framework.security.SecurityManager;
import colesico.framework.security.authentication.AuthenticationManager;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationResult;

import java.util.concurrent.Callable;

/**
 * Security context default implementation.
 * Extend this class to customize identity control.
 */
abstract public class SecurityManagerImpl implements SecurityManager {

    protected final ThreadScope threadScope;
    protected final AuthenticationManager authenticationManager;

    public SecurityManagerImpl(ThreadScope threadScope, AuthenticationManager authenticationManager) {
        this.threadScope = threadScope;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Read principle from source.
     * Override this method to fine-grained principle read control: check validity,
     * enrich with extra data from database, e.t.c.
     */
    abstract protected P read();

    @Override
    public AuthenticationResult<?> authenticate(AuthenticationRequest request) {
        var result = authenticationManager.authenticate(request);
        if (result.isSuccess()) {
            threadScope.put(IdentityHolder.SCOPE_KEY,
                    new IdentityHolder(result.getIdentity().orElseThrow()));
        }
        return result;
    }

    @Override
    public Identity<?> identity() {
        // Check thread cache at first
        IdentityHolder holder = threadScope.get(IdentityHolder.SCOPE_KEY);
        if (holder != null) {
            return holder.identity();
        } else {
            // Create temporary empty identity holder
            // for possible subsequent recursive identity() invocations
            threadScope.put(IdentityHolder.SCOPE_KEY, new IdentityHolder(null));
        }

        // No identity in cache. Retrieve identity from source
        Identity<?> identity = read();
        // Store identity to cache
        threadScope.put(IdentityHolder.SCOPE_KEY, new IdentityHolder(identity));

        return identity;
    }


    @Override
    public <T> T callAs(Callable<T> callable, Identity<?> identity) {
        IdentityHolder holder = threadScope.get(IdentityHolder.SCOPE_KEY);
        threadScope.put(IdentityHolder.SCOPE_KEY, new IdentityHolder(identity));
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadScope.put(IdentityHolder.SCOPE_KEY, holder);
        }
    }

    public record IdentityHolder(Identity<?> identity) {
        public static final Key<IdentityHolder> SCOPE_KEY = new TypeKey<>(IdentityHolder.class);
    }

}
