/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */
package colesico.framework.profile.internal;

import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.profile.DefaultProfileKit;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileConfig;
import colesico.framework.profile.ProfileKit;
import colesico.framework.profile.teleapi.ProfileTeleAssist;

import javax.inject.Singleton;
import java.util.Locale;

import static colesico.framework.ioc.Rank.RANK_MINOR;

@Producer(RANK_MINOR)
@Produce(DefaultProfileKit.class)
@Produce(ProfileConfigImpl.class)
@Produce(ProfileTeleAssistImpl.class)
public class ProfileProducer {

    @Singleton
    public ProfileKit getProfileKit(DefaultProfileKit impl) {
        return impl;
    }


    public Profile getProfile(ProfileKit kit) {
        return kit.getProfile();
    }

    public Locale getLocale(ProfileKit kit) {
        Profile profile = kit.getProfile();
        return profile != null ? profile.getLocale() : Locale.getDefault();
    }

    @Singleton
    public ProfileTeleAssist getProfileTeleAssist(ProfileTeleAssistImpl impl){
        return impl;
    }

    /**
     * Default profile config
     */
    @Singleton
    public ProfileConfig getProfileConfig(ProfileConfigImpl impl) {
        return impl;
    }

}
