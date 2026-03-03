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

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpRequest;
import colesico.framework.profile.*;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.assist.TeleHttpUtils;
import colesico.framework.telehttp.writer.ProfileWriter;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static colesico.framework.telehttp.writer.ProfileWriter.PROFILE_HEADER;

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

    protected void readLocale(ProfileAttribute<Locale> attribute, HttpRequest request, Map<String, String> profileProperties) {
        readAttribute(attribute, request, profileProperties);
        if (!attribute.hasValue()) {
            String acceptLangs = request.getHeaders().get(ACCEPT_LANGUAGE_HEADER);
            Locale locale = TeleHttpUtils.getAcceptedLanguage(acceptLangs);
            if (locale != null) {
                attribute.setValue(locale);
            }
        }
    }

    protected void readAttribute(ProfileAttribute<?> attribute, HttpRequest request, Map<String, String> profileProperties) {
        var value = profileProperties.get(attribute.name());
        if (value != null) {
            attribute.setString(value);
        }
    }

    /**
     * Override this method to dispatch reading
     */
    protected AttributeReader getAttributeReader(ProfileAttribute attribute) {
        if (attribute instanceof LocaleAttribute) {
            return this::readLocale;
        } else {
            return this::readAttribute;
        }
    }

    @Override
    public final P read(C context) {
        HttpRequest request = httpContextProv.get().getRequest();
        P profile = profileUtils.newInstance();

        Map<String, String> profileProperties = new HashMap<>();
        HttpCookie profileCookie = request.getCookies().get(ProfileWriter.PROFILE_COOKIE);
        if (profileCookie != null) {
            profileProperties.putAll(TeleHttpUtils.parseProperties(profileCookie.getValue()));
        }

        HttpCookie profileHeader = request.getCookies().get(PROFILE_HEADER);
        if (profileCookie != null) {
            profileProperties.putAll(TeleHttpUtils.parseProperties(profileHeader.getValue()));
        }

        Set<ProfileAttribute> attributes = profileUtils.getAttributes(profile);
        for (ProfileAttribute attribute : attributes) {
            if (!attribute.metadata().dataPortReadable) {
                continue;
            }
            var attributeReader = getAttributeReader(attribute);
            attributeReader.read(attribute, request, profileProperties);
        }
        return profile;
    }

    public interface AttributeReader<V> {
        void read(ProfileAttribute<V> attribute, HttpRequest request, Map<String, String> profileProperties);
    }
}
