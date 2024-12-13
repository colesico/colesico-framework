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
package colesico.framework.profile.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfilePort;

import javax.inject.Provider;
import java.util.Locale;


@Producer
@Produce(ProfileDefaultPort.class)
public class ProfileProducer {

    @Unscoped
    public Profile getProfile(ProfilePort port) {
        return port.read();
    }

    /**
     * Get current locale
     */
    @Unscoped
    public Locale getLocale(Provider<Profile> profileProv) {
        Profile profile = profileProv.get();
        return profile != null ? profile.getLocale() : Locale.getDefault();
    }

    /**
     * Produces profile port
     */
    @Unscoped
    public ProfilePort getProfileSource(ThreadScope scope, Provider<ProfileDefaultPort> defaultPortProvider) {
        ProfilePort source = scope.get(ProfilePort.SCOPE_KEY);
        if (source == null) {
            return defaultPortProvider.get();
        }
        return source;
    }
}
