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

import java.util.HashMap;
import java.util.Map;

/**
 * Profile default implementation
 */
public class ProfileImpl implements Profile {

    private final Map<Class<?>, ? super Object> attributes = new HashMap<>();
    private final Map<Class<?>, ? super Object> preferences = new HashMap<>();

    @Override
    public <T> boolean hasAttribute(Class<T> attrClass) {
        return attributes.containsKey(attrClass);
    }

    @Override
    public <A> A getAttribute(Class<A> attrClass) {
        return (A) attributes.get(attrClass);
    }

    public <A> A setAttribute(A attribute) {
        return (A) attributes.put(attribute.getClass(), attribute);
    }

    @Override
    public <T> boolean hasPreference(Class<T> prefClass) {
        return preferences.containsKey(prefClass);
    }

    @Override
    public <P> P getPreference(Class<P> prefClass) {
        return (P) preferences.get(prefClass);
    }

    @Override
    public <P> P setPreference(P preference) {
        return (P) preferences.put(preference.getClass(), preference);
    }

    public Map<Class<?>, ? super Object> getAttributes() {
        return attributes;
    }

    public Map<Class<?>, ? super Object> getPreferences() {
        return preferences;
    }
}
