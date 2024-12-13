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

package colesico.framework.example.web.localization;

import colesico.framework.profile.internal.ProfileImpl;
import colesico.framework.profile.ProfileKit;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

import java.util.Locale;

@Weblet
public class MyLocalization {

    private final ProfileKit l10n;
    private final MyDictionary translations;

    public MyLocalization(ProfileKit l10n, MyDictionary translations) {
        this.l10n = l10n;
        this.translations = translations;
    }

    // http://localhost:8080/my-localization/ru
    public HtmlResponse ru() {
        l10n.setProfile(new ProfileImpl(new Locale("ru", "RU")));
        return HtmlResponse.of("Русский");
    }

    // http://localhost:8080/my-localization/en
    public HtmlResponse en() {
        l10n.setProfile(new ProfileImpl(new Locale("en", "GB")));
        return HtmlResponse.of("English");
    }

    // http://localhost:8080/my-localization/message
    public HtmlResponse message() {
        return HtmlResponse.of(translations.hello1());
    }

    // http://localhost:8080/my-localization/inherit
    public HtmlResponse inherit() {
        l10n.setProfile(new ProfileImpl(new Locale("ru", "RU")));
        return HtmlResponse.of(translations.hello3());
    }
}
