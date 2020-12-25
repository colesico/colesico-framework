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
     * Returns the valid profile associated with the current process if the profile is present.
     * If it is impossible to determine current user profile method returns common profile {@link CommonProfileCreator}
     * Method must retrieve the profile from any source (eg from the data port)
     * then validate, enrich (if needed) and cache it for a subsequent quick return within the current thread.
     * Can throws an exception in case the profile is inconsistent.
     */
    <P extends Profile> P getProfile();

    /**
     * Associate the profile with the current process.
     * The method can store the principal to any source (eg write it to the data port to
     * store it on remote client) in order to return it on subsequent requests.
     */
    void setProfile(Profile profile);

}
