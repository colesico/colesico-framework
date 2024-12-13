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

package colesico.framework.example.translation;

import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.internal.ProfileImpl;
import colesico.framework.profile.Profile;

import java.util.Locale;

/**
 * This producer is used to mock up a Profile producing for some locales
 * For real application it is not necessary to define this producer since default Profile producing is implemented.
 */
@Producer
public class ProfileMockProducer {
    private static final Profile deProfile = new ProfileImpl(new Locale("de", "DE", "UNIX"));
    private static final Profile enProfile = new ProfileImpl(new Locale("en", "GB"));
    private static final Profile ruProfile = new ProfileImpl(new Locale("ru"));
    private static final Profile frProfile = new ProfileImpl(new Locale("fr", "FR"));


    private static Profile curProfile = deProfile;

    public static void en() {
        curProfile = enProfile;
    }

    public static void ru() {
        curProfile = ruProfile;
    }

    public static void de() {
        curProfile = deProfile;
    }

    public static void fr() {
        curProfile = frProfile;
    }

    @Substitute
    public Profile getProfile() {
        return curProfile;
    }
}
