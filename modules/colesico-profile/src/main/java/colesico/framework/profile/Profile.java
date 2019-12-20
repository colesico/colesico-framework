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

import colesico.framework.ioc.Key;
import colesico.framework.ioc.TypeKey;

import java.io.Serializable;
import java.util.Locale;

/**
 * Localization profile basic interface.
 * Defines key values for profile.
 */
public interface Profile extends Cloneable, Serializable {

    String GET_LOCALE_METHOD = "getLocale";
    String GET_QUALIFIERS_METHOD = "getQualifiers";

    Key<Profile> SCOPE_KEY = new TypeKey<>(Profile.class);

    /**
     * Returns client locale.
     *
     * @return
     */
    Locale getLocale();

    /**
     * Returns localization qualifiers.
     * Localization qualifiers is used to select appropriate localized resource, etc
     * The qualifiers in the array must be ordered in accordance with the order specified in the profile configuration
     *
     * @return profile qualifiers
     */
    String[] getQualifiers();
}
