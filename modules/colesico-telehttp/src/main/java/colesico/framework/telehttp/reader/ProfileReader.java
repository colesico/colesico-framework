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
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.assist.TeleHttpUtils;
import colesico.framework.telehttp.writer.ProfileWriter;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 * Default profile reader
 */
@Singleton
public class ProfileReader<P extends Profile, C extends HttpTRContext> implements HttpTeleReader<P, C> {

    public static final String ACCEPT_LANGUAGE_HEADER = "Accept-language";

    protected final ProfileUtils<P> profileUtils;
    protected final Provider<HttpContext> httpContextProv;

    public ProfileReader(ProfileUtils profileUtils, Provider<HttpContext> httpContextProv) {
        this.profileUtils = profileUtils;
        this.httpContextProv = httpContextProv;
    }

    protected P buildProfile(HttpRequest request) {

        String attributeTagsStr = request.getHeaders().get(ProfileWriter.ATTRIBUTES_HEADER_NAME);
        Map<String, String> attributeProps = TeleHttpUtils.parseProperties(attributeTagsStr);
        Collection<?> attributes = profileUtils.fromProperties(attributeProps);

        Map<String, String> preferenceProps;
        HttpCookie preferenceTagCookie = request.getCookies().get(ProfileWriter.PREFERENCE_COOKIE_NAME);
        if (preferenceTagCookie != null) {
            preferenceProps = TeleHttpUtils.parseProperties(preferenceTagCookie.getValue());
        } else {
            preferenceProps = Map.of();
        }
        Collection<?> preferences = profileUtils.fromProperties(preferenceProps);

        P profile = profileUtils.create(attributes, preferences);

        if (!profile.hasPreference(Locale.class)) {
            String acceptLangs = request.getHeaders().get(ACCEPT_LANGUAGE_HEADER);
            Locale locale = TeleHttpUtils.getAcceptedLanguage(acceptLangs);
            if (locale == null) {
                locale = Locale.getDefault();
            }
            profile.setAttribute(locale);
        }

        return profile;
    }


    @Override
    public final P read(C context) {
        HttpRequest request = httpContextProv.get().getRequest();
        P profile = buildProfile(request);
        return profile;
    }
}
