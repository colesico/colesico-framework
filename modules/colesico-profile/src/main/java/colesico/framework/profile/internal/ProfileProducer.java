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
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileKit;
import colesico.framework.profile.ProfileUtils;

import javax.inject.Provider;
import java.util.Locale;


@Producer
@Produce(value = ProfileUtilsImpl.class, keyType = ProfileUtils.class)
@Produce(value = ProfileKitImpl.class, keyType = ProfileKit.class)
public class ProfileProducer {

    @Unscoped
    public Profile getProfile(ProfileKit source) {
        return source.get();
    }

    /**
     * Get current locale
     */
    @Unscoped
    public Locale getLocale(Provider<Profile> profileProv) {
        Profile profile = profileProv.get();
        return profile != null ? profile.getLocale() : Locale.getDefault();
    }


}
