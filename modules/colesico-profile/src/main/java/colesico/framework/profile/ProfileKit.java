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

package colesico.framework.profile;

import colesico.framework.profile.teleapi.CommonProfileCreator;

/**
 * Profile Service.
 * This service provides centralized access to the user profile.
 */
public interface ProfileKit {

    /**
     * This method should provide current user valid profile.
     * The method should provide the ability to quickly re-read the profile within the thread.
     * If it is impassible to determine current user profile method returns common profile {@link CommonProfileCreator}
     */
    <P extends Profile> P getProfile();

    /**
     * Setup curent user profile
     *
     * @param profile
     */
    void setProfile(Profile profile);

}
