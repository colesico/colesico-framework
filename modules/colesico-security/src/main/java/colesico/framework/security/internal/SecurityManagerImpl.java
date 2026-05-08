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

import colesico.framework.security.Identity;
import colesico.framework.security.IdentityContext;
import colesico.framework.security.SecurityListener;
import colesico.framework.security.SecurityManager;
import colesico.framework.security.authentication.AuthenticationManager;
import colesico.framework.security.authentication.AuthenticationContext;
import colesico.framework.security.authentication.AuthenticationResult;

import java.util.concurrent.Callable;

/**
 * Security context default implementation.
 * Extend this class to customize identity control.
 */
abstract public class SecurityManagerImpl implements SecurityManager {

    protected final IdentityContext identityContext;
    protected final AuthenticationManager authenticationManager;
    protected final SecurityListener listener;

    public SecurityManagerImpl(IdentityContext identityContext, AuthenticationManager authenticationManager, SecurityListener listener) {
        this.identityContext = identityContext;
        this.authenticationManager = authenticationManager;
        this.listener = listener;
    }

    /**
     * Read AuthenticationRequest from source.
     * Override this method to fine-grained read control: check validity,
     * enrich with extra data, e.t.c.
     */
    abstract protected AuthenticationContext readAuthRequest();

    @Override
    public AuthenticationResult<?> authenticate(AuthenticationContext context) {
        var result = authenticationManager.authenticate(context);
        if (result.isSuccess()) {
            identityContext.setIdentity(result.getIdentity().orElseThrow());

        }
        return result;
    }

    @Override
    public Identity<?> identity() {
        // Check thread cache at first
        var holder = identityContext.holder();
        if (holder != null) {
            return holder.identity();
        } else {
            // Create temporary empty identity holder
            // for possible subsequent recursive identity() invocations
            identityContext.setIdentity(null);
        }

        // No identity in cache. Retrieve identity from source
        AuthenticationContext authRequest = readAuthRequest();
        if (authRequest != null) {
            var authResult = authenticationManager.authenticate(authRequest);
            if (authResult.isSuccess()) {
                // Store identity to cache
                var identity = authResult.getIdentity().orElseThrow();
                identityContext.setIdentity(identity);
                return identity;
            }
        }

        return null;
    }

    @Override
    public void logout() {
        var holder = identityContext.holder();
        //TODO: process identity notify event
        identityContext.clear();
    }

    @Override
    public <T> T callAs(Callable<T> callable, Identity<?> identity) {
        var previous = identityContext.holder();
        identityContext.setIdentity(identity);
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            identityContext.setIdentity(previous.identity());
        }
    }


}
