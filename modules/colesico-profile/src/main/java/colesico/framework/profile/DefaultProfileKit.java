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

import colesico.framework.ioc.Key;
import colesico.framework.ioc.ThreadScope;
import colesico.framework.ioc.TypeKey;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleReader;
import colesico.framework.teleapi.TeleWriter;

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

    protected Profile profileReadControl(DataPort<Object, Object> port) {
        return port.read(Profile.class, null);
    }

    protected void profileWriteControl(DataPort port, Profile profile) {
        port.write(Profile.class, profile, null);
    }

    @Override
    public final <P extends Profile> P getProfile() {
        // Check thread cache at first
        ProfileHolder holder = threadScope.get(ProfileHolder.SCOPE_KEY);
        if (holder != null) {
            return (P) holder.getProfile();
        }

        DataPort port = dataPortProv.get();
        Profile profile = profileReadControl(port);

        // Cache the profile
        threadScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(profile));
        return (P) profile;
    }

    @Override
    public final void setProfile(Profile profile) {
        DataPort port = dataPortProv.get();
        profileWriteControl(port, profile);
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
}
