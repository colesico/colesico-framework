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

package colesico.framework.weblet.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpRequest;
import colesico.framework.profile.Profile;
import colesico.framework.profile.teleapi.CommonProfileCreator;
import colesico.framework.profile.teleapi.ProfileSerializer;
import colesico.framework.weblet.assist.WebUtils;
import colesico.framework.weblet.teleapi.ReaderContext;
import colesico.framework.weblet.teleapi.WebletTeleReader;
import colesico.framework.weblet.teleapi.writer.ProfileWriter;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Base64;
import java.util.Locale;

/**
 * Default profile reader
 */
@Singleton
public class ProfileReader implements WebletTeleReader<Profile> {


    public static final String ACCEPT_LANGUAGE_HEADER = "Accept-language";

    protected final ProfileSerializer profileSerializer;
    protected final CommonProfileCreator commonProfileCreator;
    protected final Provider<HttpContext> httpContextProv;

    @Inject
    public ProfileReader(ProfileSerializer profileSerializer, CommonProfileCreator commonProfileCreator, Provider<HttpContext> httpContextProv) {
        this.profileSerializer = profileSerializer;
        this.commonProfileCreator = commonProfileCreator;
        this.httpContextProv = httpContextProv;
    }


    protected Profile getCustomProfile(HttpRequest request) {

        // Retrieve profile from http header
        String profileValue = request.getHeaders().get(ProfileWriter.HEADER_NAME);
        if (StringUtils.isBlank(profileValue)) {
            // Retrieve profile from http cookie
            HttpCookie cookie = request.getCookies().get(ProfileWriter.COOKIE_NAME);
            profileValue = cookie != null ? cookie.getValue() : null;
            if (StringUtils.isBlank(profileValue)) {
                return null;
            }
        }

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] profileBytes = decoder.decode(profileValue);

        Profile profile = profileSerializer.deserialize(profileBytes);
        return profile;
    }

    protected Profile getCommonProfile(HttpRequest request) {
        String accLangs = request.getHeaders().get(ACCEPT_LANGUAGE_HEADER);
        Locale locale = WebUtils.getAcceptedLanguage(accLangs);
        if (locale == null) {
            locale = Locale.getDefault();
        }

        return commonProfileCreator.createCommonProfile(locale);
    }

    @Override
    public final Profile read(ReaderContext context) {
        HttpRequest request = httpContextProv.get().getRequest();
        Profile profile = getCustomProfile(request);
        if (profile == null) {
            profile = getCommonProfile(request);
        }
        return profile;
    }
}
