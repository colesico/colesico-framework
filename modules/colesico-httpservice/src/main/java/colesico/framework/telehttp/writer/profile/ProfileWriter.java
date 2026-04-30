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

package colesico.framework.telehttp.writer.profile;

import colesico.framework.http.CookieFactory;
import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpResponse;
import colesico.framework.profile.Profile;
import colesico.framework.profile.assist.LocaleAttribute;
import colesico.framework.telehttp.HttpTWContext;
import colesico.framework.telehttp.AbstractHttpTeleWriter;
import colesico.framework.telehttp.assist.TeleHttpUtils;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.*;

/**
 *  Profile default writer
 */
@Singleton
public class ProfileWriter<P extends Profile, C extends HttpTWContext<?, ?>> extends AbstractHttpTeleWriter<P, C> {

    public static final String PROFILE_COOKIE = "profile";
    public static final String PROFILE_HEADER = "X-Profile";

    protected final ProfileHttpConfigPrototype config;
    protected final CookieFactory cookieFactory;

    public ProfileWriter(Provider<HttpContext> httpContextProv,
                         ProfileHttpConfigPrototype config,
                         CookieFactory cookieFactory) {
        super(httpContextProv);
        this.config = config;
        this.cookieFactory = cookieFactory;
    }

    /**
     *  Override this method to process different profile type
     */
    protected void exportToAttributes(P profile, Map<String, String> attributes) {
        var localeAttribute = LocaleAttribute.of(profile);
        attributes.put(localeAttribute.name(), localeAttribute.asString());
    }

    @Override
    public final void write(P profile, C wrContext) {
        // Calc expiring
        Calendar expires = Calendar.getInstance();
        String profileStr;
        if (profile != null) {
            Map<String, String> attributes = new HashMap<>();
            exportToAttributes(profile, attributes);
            profileStr = TeleHttpUtils.stringifyAttributes(attributes);
            expires.add(Calendar.DAY_OF_MONTH, config.cookieValidityDays());
        } else {
            profileStr = null;
            expires.add(Calendar.DAY_OF_MONTH, -1);
        }

        HttpCookie cookie = cookieFactory.create(PROFILE_COOKIE, profileStr);
        cookie.setExpires(expires.getTime()).setSameSite(HttpCookie.SameSite.STRICT);

        HttpResponse response = httpContextProv.get().response();
        response.setCookie(cookie);
        response.setHeader(PROFILE_HEADER, profileStr);

    }
}
