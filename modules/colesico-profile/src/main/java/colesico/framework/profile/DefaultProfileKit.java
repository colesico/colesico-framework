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

package colesico.framework.profile;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TRContext;
import colesico.framework.teleapi.TWContext;

import javax.inject.Provider;

/**
 * Profile kit default implementation.
 * Extend this class to customize the profile read/write control.
 * (For example to enrich the profile from the database)
 */
public class DefaultProfileKit implements ProfileKit {

    protected final ThreadScope threadScope;
    protected final Provider<DataPort> dataPortProv;

    public DefaultProfileKit(ThreadScope threadScope, Provider<DataPort> dataPortProv) {
        this.threadScope = threadScope;
        this.dataPortProv = dataPortProv;
    }

    /**
     * Controls the profile read from the data port.
     * Override this method to get more specific control.
     * This method is used to fine grained control of profile: check validity, enrich with extra data, e.t.c.
     *
     * @return Valid profile or null
     */
    protected InputControlResult controlInputProfile(Profile profile) {
        return InputControlResult.accept(profile);
    }

    /**
     * Controls the profile before write to the data port.
     * Override this method to get more specific control.
     */
    protected Profile controlOutputProfile(Profile profile) {
        return profile;
    }

    @Override
    public final <P extends Profile> P getProfile() {
        // Check thread cache at first
        ProfileHolder holder = threadScope.get(ProfileHolder.SCOPE_KEY);
        if (holder != null) {
            return (P) holder.profile();
        } else {
            // Create temporary empty profile holder
            // for possible subsequent recursive getProfile() invocations
            threadScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(null));
        }

        // No profile in cache. Retrieve profile from data port

        DataPort<TRContext, TWContext> port = dataPortProv.get();
        Profile profile = port.read(Profile.class);

        // Control profile
        InputControlResult result = controlInputProfile(profile);
        profile = result.profile;
        if (!result.accepted) {
            port.write(profile, Profile.class);
        }

        // Store profile to cache
        threadScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(profile));

        return (P) profile;
    }

    @Override
    public final void setProfile(Profile profile) {
        DataPort port = dataPortProv.get();
        profile = controlOutputProfile(profile);
        port.write(profile, Profile.class);
        threadScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(profile));
    }

    public record ProfileHolder(Profile profile) {
        public static final Key<DefaultProfileKit.ProfileHolder> SCOPE_KEY = new TypeKey<>(ProfileHolder.class);
    }

    public record InputControlResult(Profile profile, boolean accepted) {

        public static InputControlResult reset(Profile profile) {
            return new InputControlResult(profile, false);
        }

        public static InputControlResult accept(Profile profile) {
            return new InputControlResult(profile, true);
        }
    }

}
