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

import java.util.Locale;
import java.util.Set;

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

    protected void readLocale(HttpRequest request, ProfileAttribute<Locale> attribute) {
        HttpCookie prefCookie = request.getCookies().get(ProfileWriter.PREFERENCE_COOKIE_NAME);
        if (prefCookie != null) {
            attribute.setString(prefCookie.getValue());
        } else {
            String acceptLangs = request.getHeaders().get(ACCEPT_LANGUAGE_HEADER);
            Locale locale = TeleHttpUtils.getAcceptedLanguage(acceptLangs);
            if (locale != null) {
                attribute.setValue(locale);
            }
        }
    }

    protected void readAttribute(HttpRequest request, ProfileAttribute attribute) {
        String headerName=""
    }

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
        Set<ProfileAttribute> attributes = profileUtils.getAttributes(profile);
        for (ProfileAttribute attribute : attributes) {
            var attributeReader = getAttributeReader(attribute);
            attributeReader.read(request, attribute);
        }
        return profile;
    }

    public interface AttributeReader<V> {
        void read(HttpRequest request, ProfileAttribute<V> attribute);
    }
}
