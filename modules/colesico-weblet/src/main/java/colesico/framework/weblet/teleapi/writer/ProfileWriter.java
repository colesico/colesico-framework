/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.weblet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpResponse;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileConfig;
import colesico.framework.profile.teleapi.ProfileTeleAssist;
import colesico.framework.weblet.teleapi.ProfileWebletConfig;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.WriterContext;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Base64;
import java.util.Calendar;

@Singleton
public class ProfileWriter implements WebletTeleWriter<Profile> {

    public static final String COOKIE_NAME = "profile";
    public static final String HEADER_NAME = "Localization";

    protected final ProfileWebletConfig config;
    protected final ProfileTeleAssist profileTeleAssist;
    protected final Provider<HttpContext> httpContextProv;

    public ProfileWriter(ProfileWebletConfig config, ProfileTeleAssist profileTeleAssist, Provider<HttpContext> httpContextProv) {
        this.config = config;
        this.profileTeleAssist = profileTeleAssist;
        this.httpContextProv = httpContextProv;
    }

    @Override
    public final void write(Profile profile, WriterContext wrContext) {
        // Calc expiring
        Calendar expires = Calendar.getInstance();
        String profileValue;
        if (profile != null) {
            byte[] profileBytes = profileTeleAssist.serialize(profile);
            Base64.Encoder encoder = Base64.getEncoder();
            profileValue = encoder.encodeToString(profileBytes);
            expires.add(Calendar.DAY_OF_MONTH, config.getCookieValidityDays());
        } else {
            profileValue = null;
            expires.add(Calendar.DAY_OF_MONTH, -1);
        }

        HttpCookie cookie = new HttpCookie(COOKIE_NAME, profileValue);
        cookie.setExpires(expires.getTime()).setSameSite(HttpCookie.SameSite.STRICT);

        HttpResponse response = httpContextProv.get().getResponse();
        response.setCookie(cookie);
        response.setHeader(HEADER_NAME, profileValue);

    }
}
