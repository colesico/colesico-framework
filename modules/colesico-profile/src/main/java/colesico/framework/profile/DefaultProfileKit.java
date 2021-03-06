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

    protected boolean isInputControlRequired(Profile profile) {
        return false;
    }

    /**
     * Controls the profile read from the data port.
     * Override this method to get more specific control.
     * This method is used to fine grained control of profile: check validity, enrich with extra data, e.t.c.
     *
     * @return Valid profile or null
     */
    protected InputControlResult controlInputProfile(Profile profile) {
        throw new UnsupportedOperationException("Default control not implemented");
    }

    protected boolean isOutputControlRequired(Profile profile) {
        return false;
    }

    /**
     * Controls the profile before write to the data port.
     * Override this method to get more specific control.
     */
    protected Profile controlOutputProfile(Profile profile) {
        throw new UnsupportedOperationException("Default control not implemented");
    }

    @Override
    public final <P extends Profile> P getProfile() {
        // Check thread cache at first
        ProfileHolder holder = threadScope.get(ProfileHolder.SCOPE_KEY);
        if (holder != null) {
            return (P) holder.getProfile();
        } else {
            // Create temporary empty profile holder
            // for possible subsequent recursive getProfile() invocations
            threadScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(null));
        }

        // No profile in cache. Retrieve profile from data port

        DataPort<Object, Object> port = dataPortProv.get();
        Profile profile = port.read(Profile.class, null);

        // Is control needed?

        if (isInputControlRequired(profile)) {
            InputControlResult res = controlInputProfile(profile);
            profile = res.getProfile();
            if (res.isUpdateOnClient()) {
                port.write(Profile.class, profile, null);
            }
        }

        // Store profile to cache
        threadScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(profile));

        return (P) profile;
    }

    @Override
    public final void setProfile(Profile profile) {
        DataPort port = dataPortProv.get();
        if (isOutputControlRequired(profile)) {
            profile = controlOutputProfile(profile);
        }

        port.write(Profile.class, profile, null);
        threadScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(profile));
    }

    public static final class ProfileHolder {
        public static final Key<DefaultProfileKit.ProfileHolder> SCOPE_KEY = new TypeKey<>(ProfileHolder.class);

        private final Profile profile;

        public ProfileHolder(Profile profile) {
            this.profile = profile;
        }

        public Profile getProfile() {
            return profile;
        }
    }

    public static final class InputControlResult {

        /**
         * Actual profile to be used
         */
        private Profile profile = null;

        /**
         * Whether or not to update the profile on the client
         */
        private boolean updateOnClient = false;

        public InputControlResult() {
        }

        public InputControlResult(Profile profile, boolean updateOnClient) {
            this.profile = profile;
            this.updateOnClient = updateOnClient;
        }

        public InputControlResult(Profile profile) {
            this.profile = profile;
            this.updateOnClient = true;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public boolean isUpdateOnClient() {
            return updateOnClient;
        }

        public void setUpdateOnClient(boolean updateOnClient) {
            this.updateOnClient = updateOnClient;
        }

    }
}
