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
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileUtils;

import java.util.Locale;

/**
 * This producer is used to mock up a Profile producing for some locales
 * For real application it is not necessary to define this producer since default Profile producing is implemented.
 */
@Producer
public class ProfileMockProducer {
    private static final Locale deLocale = new Locale("de", "DE", "UNIX");
    private static final Locale enLocale = new Locale("en", "GB");
    private static final Locale ruLocale = new Locale("ru");
    private static final Locale frLocale = new Locale("fr", "FR");


    private static Locale curLocale = deLocale;

    public static void en() {
        curLocale = enLocale;
    }

    public static void ru() {
        curLocale = ruLocale;
    }

    public static void de() {
        curLocale = deLocale;
    }

    public static void fr() {
        curLocale = frLocale;
    }

    @Substitute
    public Profile getProfile(ProfileUtils profileUtils) {
        return profileUtils.fromLocale(curLocale);
    }
}
