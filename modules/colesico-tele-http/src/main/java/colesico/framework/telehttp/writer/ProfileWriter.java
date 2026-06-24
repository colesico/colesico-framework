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

package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpCookieFactory;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpResponse;
import colesico.framework.profile.Profile;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.assist.TeleHttpUtils;
import jakarta.inject.Singleton;

import java.util.*;

/**
 * Profile default writer
 */
@Singleton
public class ProfileWriter<P extends Profile, R extends HttpWriteOptions> implements TeleHttpWriter<P, R> {

    public static final String PROFILE_COOKIE = "profile";
    public static final String PROFILE_HEADER = "x-profile";
    public static final String LOCALE_ATTRIBUTE = "locale";

    protected final ProfileWriterConfigPrototype config;
    protected final HttpCookieFactory cookieFactory;

    public ProfileWriter(ProfileWriterConfigPrototype config,
                         HttpCookieFactory cookieFactory) {
        this.config = config;
        this.cookieFactory = cookieFactory;
    }

    @Override
    public void write(P profile, R options) {
        // Calc expiring
        Calendar expires = Calendar.getInstance();
        String profileStr;
        if (profile != null) {
            Map<String, String> attributes = new HashMap<>();
            toAttributes(profile, attributes);
            profileStr = TeleHttpUtils.stringifyAttributes(attributes);
            expires.add(Calendar.DAY_OF_MONTH, config.cookieValidityDays());
        } else {
            profileStr = null;
            expires.add(Calendar.DAY_OF_MONTH, -1);
        }

        HttpCookie cookie = cookieFactory.create(PROFILE_COOKIE, profileStr);
        cookie.setExpires(expires.getTime().toInstant()).setSameSite(HttpCookie.SameSite.STRICT);

        HttpResponse response = null;
        response.setCookie(cookie);
        response.setHeader(PROFILE_HEADER, profileStr);
    }

    /**
     * Override this method to process different profile type
     */
    protected void toAttributes(P profile, Map<String, String> attributes) {
        exportLocale(profile, attributes);
    }

    protected void exportLocale(P profile, Map<String, String> attributes) {
        if (profile.locale() != null) {
            attributes.put(LOCALE_ATTRIBUTE, profile.locale().toLanguageTag());
        }
    }

}
