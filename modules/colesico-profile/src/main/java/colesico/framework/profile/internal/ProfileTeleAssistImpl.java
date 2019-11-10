/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.profile.internal;

import colesico.framework.profile.DefaultProfile;
import colesico.framework.profile.Profile;
import colesico.framework.profile.teleapi.ProfileTeleAssist;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * Default profile tele-assistant
 */
@Singleton
public class ProfileTeleAssistImpl implements ProfileTeleAssist<DefaultProfile> {

    @Override
    public byte[] serialize(DefaultProfile profile) {
        StringBuilder sb = new StringBuilder();
        sb.append(profile.getLocale().getLanguage());
        sb.append('|');
        sb.append(profile.getLocale().getCountry());
        try {
            return sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DefaultProfile deserialize(byte[] profileBytes) {
        try {
            String localeStr = new String(profileBytes, "UTF-8");
            String[] localeItems = StringUtils.split(localeStr, "|");
            Locale locale = new Locale(localeItems[0], localeItems[1]);
            return new DefaultProfile(locale);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile buildDefault(Locale locale) {
        return new DefaultProfile(locale);
    }

}
