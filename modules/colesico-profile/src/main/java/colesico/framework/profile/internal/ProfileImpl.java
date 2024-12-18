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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Profile default implementation
 */
public class ProfileImpl implements Profile {

    private final Map<Class<?>, ? super Object> attributesMap = new HashMap<>();
    private final Map<Class<?>, ? super Object> preferencesMap = new HashMap<>();

    @Override
    public <T> boolean hasAttribute(Class<T> attrClass) {
        return attributesMap.containsKey(attrClass);
    }

    @Override
    public <A> A getAttribute(Class<A> attrClass) {
        return (A) attributesMap.get(attrClass);
    }

    public <A> A setAttribute(A attribute) {
        return (A) attributesMap.put(attribute.getClass(), attribute);
    }

    public Collection<?> getAttributes() {
        return attributesMap.values();
    }

    @Override
    public <T> boolean hasPreference(Class<T> prefClass) {
        return preferencesMap.containsKey(prefClass);
    }

    @Override
    public <P> P getPreference(Class<P> prefClass) {
        return (P) preferencesMap.get(prefClass);
    }

    @Override
    public <P> P setPreference(P preference) {
        return (P) preferencesMap.put(preference.getClass(), preference);
    }

    public Collection<?> getPreferences() {
        return preferencesMap.values();
    }

    public Map<Class<?>, ? super Object> getAttributesMap() {
        return attributesMap;
    }

    public Map<Class<?>, ? super Object> getPreferencesMap() {
        return preferencesMap;
    }
}
