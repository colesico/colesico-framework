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

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

import java.util.Locale;

/**
 * User localization profile.
 * <p>
 * Extends this class to add more profile properties.
 * </p>
 * The profile contains information necessary to adapt
 * the application to the user. For example - locale, time zone,
 * user terminal type (mobil/pc), various user preferences.
 * The specific implementation depends on the needs of the application and
 * has to be implemented in the application.
 * <p>
 * Attributes are assigned on the calling side or with {@link ProfileListener}
 */
public interface Profile {

    /**
     * Scope key for caching profile
     */
    Key<Profile> SCOPE_KEY = new TypeKey<>(Profile.class);

    /**
     * Returns user locale.
     */
    Locale getLocale();

    /**
     * Set profile locale
     */
    void setLocale(Locale locale);

}
