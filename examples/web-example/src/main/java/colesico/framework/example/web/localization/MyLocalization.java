/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.example.web.localization;

import colesico.framework.profile.ProfileManager;
import colesico.framework.profile.ProfileSource;
import colesico.framework.weblet.Weblet;

import java.util.Locale;

@Weblet
public class MyLocalization {

    private final ProfileManager profileManager;

    private final MyDictionary translations;

    public MyLocalization(ProfileManager profileManager, MyDictionary translations) {
        this.profileManager = profileManager;
        this.translations = translations;
    }

    // http://localhost:8080/my-localization/message
    public String message() {
        return translations.hello1();
    }

    // http://localhost:8080/my-localization/ru
    public String ru() {
        var profile = profileManager.resolve();
        profile.setLocale(Locale.of("ru", "RU"));
        profileManager.commit(profile);
        return "Русский";
    }

    // http://localhost:8080/my-localization/en
    public String en() {
        var profile = profileManager.resolve();
        profile.setLocale(Locale.of("en", "GB"));
        profileManager.commit(profile);
        return "English";
    }

    // http://localhost:8080/my-localization/inherit
    public String inherit() {
        var profile = profileManager.resolve();
        profile.setLocale(Locale.of("ru", "RU"));
        profileManager.commit(profile);
        return translations.hello3();
    }
}
