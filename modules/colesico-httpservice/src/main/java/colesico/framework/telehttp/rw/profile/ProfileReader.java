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

package colesico.framework.telehttp.rw.profile;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.profile.*;
import colesico.framework.profile.assist.LocaleProperty;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.assist.TeleHttpUtils;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.*;

import static colesico.framework.telehttp.rw.profile.ProfileWriter.PROFILE_HEADER;

/**
 * Profile default reader
 */
@Singleton
public class ProfileReader<P extends Profile, C extends HttpTRContext> implements HttpTeleReader<P, C> {

    public static final String ACCEPT_LANGUAGE_HEADER = "Accept-language";

    protected final Provider<HttpContext> httpContextProv;
    protected final ProfileHttpConfigPrototype config;

    public ProfileReader(Provider<HttpContext> httpContextProv, ProfileHttpConfigPrototype config) {
        this.httpContextProv = httpContextProv;
        this.config = config;
    }

    protected void readLocale(ProfileAttribute<Profile, Locale> attribute, HttpRequest request, Map<String, String> profileProperties) {
        readAttribute(attribute, request, profileProperties);
        if (!attribute.hasValue()) {
            String acceptLangs = request.headers().get(ACCEPT_LANGUAGE_HEADER);
            Locale locale = TeleHttpUtils.getAcceptedLanguage(acceptLangs);
            if (locale != null) {
                attribute.setValue(locale);
            }
        }
    }

    protected void readAttribute(ProfileAttribute<?, ?> attribute, HttpRequest request, Map<String, String> profileProperties) {
        var value = profileProperties.get(attribute.name());
        if (value != null) {
            attribute.setString(value);
        }
    }

    /**
     *  Override this method to process different profile type
     */
    protected void importFromProperties(P profile, Map<String, String> properties,HttpRequest request) {
        var localeProperty = LocaleProperty.of(profile);
        properties.put(localeProperty.name(), localeProperty.asString());
    }

    @Override
    public final P read(C context) {
        HttpRequest request = httpContextProv.get().request();

        Map<String, String> properties = new HashMap<>();
        var profileCookie = request.cookies().get(ProfileWriter.PROFILE_COOKIE);
        if (profileCookie != null) {
            properties.putAll(TeleHttpUtils.parseProperties(profileCookie.setValue()));
        }

        var profileHeader = request.cookies().get(PROFILE_HEADER);
        if (profileHeader != null) {
            properties.putAll(TeleHttpUtils.parseProperties(profileHeader.setValue()));
        }

        P profile = (P) context.attributes();
        importFromProperties(profile,properties);
        return profile;
    }

}
