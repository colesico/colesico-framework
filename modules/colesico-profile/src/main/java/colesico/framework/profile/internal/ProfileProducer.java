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
package colesico.framework.profile.internal;

import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.profile.*;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.Locale;

import static colesico.framework.ioc.conditional.Substitution.STUB;


@Producer
@Produce(value = ProfileSourceImpl.class, keyType = ProfileSource.class)
@Produce(value = DefaultProfileFactory.class, keyType = ProfileFactory.class, substitute = STUB)
public class ProfileProducer {

    @Unscoped
    public Profile getProfile(ProfileSource ps) {
        return ps.read();
    }

    /**
     * Get current locale
     */
    @Unscoped
    public Locale getLocale(Provider<Profile> profileProv) {
        Profile profile = profileProv.get();
        return profile != null ? profile.getLocale() : Locale.getDefault();
    }

    @Singleton
    @Substitute(STUB)
    public ProfileListener getProfileListener() {
        return new ProfileListener() {
        };
    }

}
