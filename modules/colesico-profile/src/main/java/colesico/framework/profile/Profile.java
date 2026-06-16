/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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
import java.util.Objects;

/**
 * User localization profile.
 * <p>
 * Extends this class to add more properties.
 * </p>
 * The profile contains information necessary to adapt
 * the application to the user. For example - locale, time zone,
 * user terminal type (mobil/pc), various user preferences.
 * The specific implementation depends on the needs of the application and
 * has to be implemented in the application.
 * <p>
 */
public interface Profile<ID> {

    /**
     * Profile id.
     * Typically, is a userId, serviceId, etc
     */
    ID id();

    Locale locale();

    void setLocale(Locale locale);

    default ID getId() {
        return id();
    }

    default Locale getLocale() {
        return locale();
    }

    /**
     * Profile default implementation
     */
    class Default<ID> implements Profile<ID> {

        protected ID id;
        protected Locale locale;

        public Default() {
        }

        public Default(ID id, Locale locale) {
            this.id = id;
            this.locale = locale;
        }

        public Locale locale() {
            return locale;
        }

        @Override
        public void setLocale(Locale locale) {
            this.locale = locale;
        }

        public ID id() {
            return id;
        }

        public void setId(ID id) {
            this.id = id;
        }
    }
}
