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

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;
import colesico.framework.profile.internal.ProfileImpl;

import java.util.Collection;
import java.util.Locale;

/**
 * User localization profile.
 * The profile contains information necessary to adapt
 * the application to the user. For example - localization (translation),
 * localization of resources, user terminal type, various user preferences.
 * The specific implementation depends on the needs of the application and
 * has to be implemented in the application.
 * Framework provides default implementation {@link ProfileImpl}
 */
public interface Profile {

    String GET_LOCALE_METHOD = "getLocale";

    /**
     * Scope key for caching profile
     */
    Key<Profile> SCOPE_KEY = new TypeKey<>(Profile.class);

    <T> boolean hasAttribute(Class<T> attrClass);

    /**
     * Attributes are any values that define the profiling configuration.
     * Examples of attributes may be the user's time zone, terminal type (mobile, PC), etc.
     * Attributes are assigned on the calling side and not returned back
     * (not writed to {@link colesico.framework.teleapi.DataPort} ).
     */
    <T> T getAttribute(Class<T> attrClass);

    /**
     * All attributes
     */
    Collection<?> getAttributes();

    <T> T setAttribute(T attribute);

    <T> boolean hasPreference(Class<T> prefClass);

    /**
     * Preferences are similar to attributes, but they writed to
     * For example, interface theme, language, etc.
     */
    <T> T getPreference(Class<T> prefClass);

    /**
     * All preferences
     */
    Collection<?> getPreferences();

    /**
     * @return previously preference or null
     */
    <T> T setPreference(T preference);

    /**
     * Has preference or attribute
     */
    default <T> boolean hasProperty(Class<T> propClass) {
        return hasPreference(propClass) || hasAttribute(propClass);
    }

    /**
     * Get preference or attribute
     */
    default <T> T getProperty(Class<T> propClass) {
        if (hasPreference(propClass)) {
            return getPreference(propClass);
        }
        return getAttribute(propClass);
    }

    /**
     * Returns user locale.
     */
    default Locale getLocale() {
        return getProperty(Locale.class);
    }

    /**
     * Change locale
     */
    default Locale setLocale(Locale locale) {
        return setPreference(locale);
    }

}
