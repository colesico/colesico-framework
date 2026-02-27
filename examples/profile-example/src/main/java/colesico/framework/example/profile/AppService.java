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

package colesico.framework.example.profile;


import colesico.framework.example.profile.custom.CustomProfile;
import colesico.framework.profile.ProfileSource;
import colesico.framework.service.Service;

import java.util.Locale;
import java.util.TimeZone;

@Service
public class AppService {

    private final ProfileSource<CustomProfile> profileSource;

    public AppService(ProfileSource profileSource) {
        this.profileSource = profileSource;
    }

    public CustomProfile getProfile() {
        return profileSource.profile();
    }

    public void setLocale(Locale locale) {
        CustomProfile profile = profileSource.profile();
        profile.setLocale(locale);
        profileSource.commit(profile);
    }

    public void setTimezone(TimeZone tz) {
        CustomProfile profile = profileSource.profile();
        profile.setTimeZone(tz);
        profileSource.commit(profile);
    }
}
