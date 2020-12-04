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

package colesico.framework.profile.internal;

import colesico.framework.profile.DefaultProfile;
import colesico.framework.profile.Profile;
import colesico.framework.profile.teleapi.ProfileSerializer;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Default profile tele-assistant
 */
@Singleton
public class ProfileSerializerImpl implements ProfileSerializer<DefaultProfile> {

    @Override
    public byte[] serialize(DefaultProfile profile) {
        return profile.getLocale().toLanguageTag().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public DefaultProfile deserialize(byte[] profileBytes) {
        String localeStr = new String(profileBytes, StandardCharsets.UTF_8);
        return new DefaultProfile(Locale.forLanguageTag(localeStr));
    }

}
