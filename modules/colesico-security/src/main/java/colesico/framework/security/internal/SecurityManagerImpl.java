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
import colesico.framework.security.authentication.*;
import colesico.framework.security.SecurityManager;
import colesico.framework.security.authentication.AuthenticationSource;

import java.util.concurrent.Callable;

/**
 * Security context default implementation.
 * Extend this class to customize identity control.
 */
public class SecurityManagerImpl implements SecurityManager {

    protected final IdentityContext identityContext;
    protected final AuthenticationManager authenticationManager;
    protected final AuthenticationListener authenticationListener;
    protected final AuthenticationSource authenticationSource;

    public SecurityManagerImpl(IdentityContext identityContext,
                               AuthenticationManager authenticationManager,
                               AuthenticationListener authenticationListener,
                               AuthenticationSource authenticationSource) {
        this.identityContext = identityContext;
        this.authenticationManager = authenticationManager;
        this.authenticationListener = authenticationListener;
        this.authenticationSource = authenticationSource;
    }

    @Override
    public AuthenticationResult<?> authenticate(AuthenticationContext context) {
        var result = authenticationManager.authenticate(context);
        result = authenticationListener.onAuthenticate(result);
        if (result.isSuccess()) {
            if (result.identity() == null) {
                throw new SecurityException("Null Identity for success authentication");
            }
            identityContext.setIdentity(result.identity());
        }
        return result;
    }

    @Override
    public Identity<?> identity() {
        // Check thread cache at first
        var entry = identityContext.get();
        if (entry != null) {
            return entry.identity();
        }
        // Create temporary empty identity holder
        // for possible subsequent recursive identity() invocations
        identityContext.setIdentity(null);

        // No identity in cache. Retrieve identity from source
        AuthenticationContext context = authenticationSource.context();
        if (context != null) {
            var authResult = authenticate(context);
            if (authResult.isSuccess()) {
                return authResult.identity();
            }
        }

        return null;
    }

    @Override
    public void logout() {
        var entry = identityContext.get();
        identityContext.clear();

        if (entry != null && entry.identity() != null) {
            authenticationManager.logout(entry.identity());
            authenticationSource.onLogout(entry.identity());
            authenticationListener.onLogout(entry.identity());
        }
    }

    @Override
    public <T> T callAs(Callable<T> callable, Identity<?> identity) {
        var previous = identityContext.get();
        identityContext.setIdentity(identity);
        try {
            return callable.call();
        } catch (Exception e) {
            throw (e instanceof RuntimeException re) ? re : new RuntimeException(e);
        } finally {
            if (previous != null) {
                identityContext.setEntry(previous);
            } else {
                identityContext.clear();
            }
        }
    }

}
