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

package colesico.framework.telehttp.writer;

import colesico.framework.http.CookieFactory;
import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpResponse;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileUtils;
import colesico.framework.telehttp.HttpTWContext;
import colesico.framework.telehttp.HttpTeleWriter;
import colesico.framework.telehttp.ProfileHttpConfigPrototype;
import colesico.framework.telehttp.assist.TeleHttpUtils;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

@Singleton
public final class ProfileWriter<P extends Profile, C extends HttpTWContext> extends HttpTeleWriter<P, C> {

    public static final String PREFERENCE_COOKIE_NAME = "profile";
    public static final String ATTRIBUTES_HEADER_NAME = "X-Profile";

    protected final ProfileHttpConfigPrototype config;
    protected final ProfileUtils<P> profileUtils;
    protected final CookieFactory cookieFactory;

    public ProfileWriter(Provider<HttpContext> httpContextProv,
                         ProfileHttpConfigPrototype config, ProfileUtils profileUtils,
                         CookieFactory cookieFactory) {
        super(httpContextProv);
        this.config = config;
        this.profileUtils = profileUtils;
        this.cookieFactory = cookieFactory;
    }

    @Override
    public final void write(P profile, C wrContext) {
        // Calc expiring
        Calendar expires = Calendar.getInstance();
        String preferenceStr;
        if (profile != null) {
            Collection<?> preferences = profileUtils.getPreferences(profile);
            Map<String, String> preferenceProps = profileUtils.toTags(preferences);
            preferenceStr = TeleHttpUtils.stringifyTags(preferenceProps);
            expires.add(Calendar.DAY_OF_MONTH, config.getCookieValidityDays());
        } else {
            preferenceStr = null;
            expires.add(Calendar.DAY_OF_MONTH, -1);
        }

        HttpCookie cookie = cookieFactory.create(PREFERENCE_COOKIE_NAME, preferenceStr);
        cookie.setExpires(expires.getTime()).setSameSite(HttpCookie.SameSite.STRICT);

        HttpResponse response = httpContextProv.get().getResponse();
        response.setCookie(cookie);
        response.setHeader(ATTRIBUTES_HEADER_NAME, preferenceStr);

    }
}
