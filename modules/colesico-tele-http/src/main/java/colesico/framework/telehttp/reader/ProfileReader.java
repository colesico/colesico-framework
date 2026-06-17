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

package colesico.framework.telehttp.reader;

import colesico.framework.http.HttpRequest;
import colesico.framework.profile.*;
import colesico.framework.telehttp.HttpReadOptions;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.assist.TeleHttpUtils;
import colesico.framework.telehttp.writer.ProfileWriter;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.*;

import static colesico.framework.assist.StringUtils.isBlank;
import static colesico.framework.telehttp.writer.ProfileWriter.LOCALE_ATTRIBUTE;
import static colesico.framework.telehttp.writer.ProfileWriter.PROFILE_HEADER;

/**
 * Profile default reader
 */
@Singleton
public class ProfileReader<P extends Profile<?>> implements HttpTeleReader<P, HttpReadOptions> {

    public static final String ACCEPT_LANGUAGE_HEADER = "Accept-language";

    protected final Provider<HttpRequest> httpRequest;

    @Inject
    public ProfileReader(Provider<HttpRequest> httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public final P read(Class<P> valueType, HttpReadOptions options) {
        HttpRequest request = httpRequest.get();
        Map<String, String> attributes = readProfileAttributes(request);
        return buildProfile(options, attributes, request);
    }

    /**
     * Override this method to process different profile type
     */
    protected P buildProfile(HttpReadOptions options, Map<String, String> attributes, HttpRequest request) {
        var locale = readLocale(attributes, request);
        return (P) new Profile.Default(options.attachment(), locale);
    }

    protected Locale readLocale(Map<String, String> attributes, HttpRequest request) {
        var localeTag = attributes.get(LOCALE_ATTRIBUTE);
        if (isBlank(localeTag)) {
            String acceptLangs = request.headers().get(ACCEPT_LANGUAGE_HEADER);
            Locale locale = TeleHttpUtils.acceptedLanguage(acceptLangs);
            if (locale != null) {
                return locale;
            }
            return Locale.getDefault();
        } else {
            return Locale.forLanguageTag(localeTag);
        }
    }

    private static Map<String, String> readProfileAttributes(HttpRequest request) {
        Map<String, String> attributes = new HashMap<>();
        var profileCookie = request.cookies().get(ProfileWriter.PROFILE_COOKIE);
        if (profileCookie != null) {
            attributes.putAll(TeleHttpUtils.parseAttributes(profileCookie.value()));
        }

        var profileHeader = request.cookies().get(PROFILE_HEADER);
        if (profileHeader != null) {
            attributes.putAll(TeleHttpUtils.parseAttributes(profileHeader.value()));
        }
        return attributes;
    }

}
