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

package colesico.framework.telehttp.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpRequest;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileUtils;
import colesico.framework.profile.teleapi.CommonProfileCreator;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.assist.TeleHttpUtils;
import colesico.framework.telehttp.writer.ProfileWriter;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Base64;
import java.util.Locale;

/**
 * Default profile reader
 */
@Singleton
public class ProfileReader<P extends Profile, C extends HttpTRContext> implements HttpTeleReader<P, C> {

    public static final String ACCEPT_LANGUAGE_HEADER = "Accept-language";

    protected final ProfileUtils<P> profileSerializer;
    protected final CommonProfileCreator commonProfileCreator;
    protected final Provider<HttpContext> httpContextProv;

    public ProfileReader(ProfileUtils profileUtils, CommonProfileCreator commonProfileCreator, Provider<HttpContext> httpContextProv) {
        this.profileSerializer = profileUtils;
        this.commonProfileCreator = commonProfileCreator;
        this.httpContextProv = httpContextProv;
    }

    protected P getCustomProfile(HttpRequest request) {

        // Retrieve profile from http header
        String profileValue = request.getHeaders().get(ProfileWriter.HEADER_NAME);
        if (StringUtils.isBlank(profileValue)) {
            // Retrieve profile from http cookie
            HttpCookie cookie = request.getCookies().get(ProfileWriter.COOKIE_NAME);
            if (cookie == null) {
                return null;
            }
            profileValue = cookie.getValue();
        }

        if (StringUtils.isBlank(profileValue)) {
            return null;
        }

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] profileBytes = decoder.decode(profileValue);

        P profile = profileSerializer.deserialize(profileBytes);
        return profile;
    }

    protected P getDefaultProfile(HttpRequest request) {
        String accLangs = request.getHeaders().get(ACCEPT_LANGUAGE_HEADER);
        Locale locale = TeleHttpUtils.getAcceptedLanguage(accLangs);
        if (locale == null) {
            locale = Locale.getDefault();
        }

        return commonProfileCreator.createCommonProfile(locale);
    }

    @Override
    public final P read(C context) {
        HttpRequest request = httpContextProv.get().getRequest();
        P profile = getCustomProfile(request);
        if (profile == null) {
            profile = getDefaultProfile(request);
        }
        return profile;
    }
}
