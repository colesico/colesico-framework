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

import colesico.framework.profile.Profile;

import java.util.*;

/**
 * Profile default implementation
 */
public class ProfileImpl implements Profile {

    private final Map<Class<?>, ? super Object> properties = new HashMap<>();

    private final Set<? super Object> attributes = new HashSet<>();

    private final Set<? super Object> preferences = new HashSet<>();

    public static ProfileImpl of() {
        return new ProfileImpl();
    }

    public static ProfileImpl of(Locale locale) {
        ProfileImpl profile = new ProfileImpl();
        profile.addAttribute(locale);
        return profile;
    }

    @Override
    public <T> boolean contains(Class<T> propertyClass) {
        return properties.containsKey(propertyClass);
    }

    @Override
    public <T> T get(Class<T> propertyClass) {
        return (T) properties.get(propertyClass);
    }

    protected <T> T addAttribute(T property) {
        attributes.add(property);
        return (T) properties.put(property.getClass(), property);
    }

    protected <T> T addPreference(T property) {
        preferences.add(property);
        return (T) properties.put(property.getClass(), property);
    }

    protected Map<Class<?>, ? super Object> getProperties() {
        return properties;
    }

    protected Set<? super Object> getAttributes() {
        return attributes;
    }

    protected Set<? super Object> getPreferences() {
        return preferences;
    }

    @Override
    public Iterator iterator() {
        return properties.values().iterator();
    }

}
