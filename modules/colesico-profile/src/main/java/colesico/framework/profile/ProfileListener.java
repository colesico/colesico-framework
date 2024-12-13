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

import java.util.Locale;

public interface ProfileListener<P extends Profile> {



      /**
     * Controls the profile before write it to the source.
     * Override this method to get more specific control.
     */
    P beforeWrite(P profile);

    /**
     * Controls the profile after read from the source.
     * Override this method to get more specific control.
     * This method is used to fine grained control of profile: check validity,
     * enrich with extra data, e.t.c.
     *
     * @return Valid profile or null
     */
    CheckResult<P> afterRead(P profile);

    record CheckResult<P extends Profile>(P profile, boolean refresh) {

        public static <P extends Profile> CheckResult<P> refresh(P profile) {
            return new CheckResult<>(profile, true);
        }

        public static <P extends Profile> CheckResult<P> accept(P profile) {
            return new CheckResult<>(profile, false);
        }
    }
}
