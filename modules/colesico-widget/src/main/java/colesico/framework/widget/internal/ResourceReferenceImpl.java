/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */
package colesico.framework.widget.internal;

import colesico.framework.widget.ResourceReference;

import java.util.*;

/**
 *
 * @author Vladlen Larionov
 */
public class ResourceReferenceImpl implements ResourceReference {

    protected final Map<String, Map<String, String>> map;

    public ResourceReferenceImpl() {
        map = new HashMap<>();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void add(String resourceType, String resourceId, String reference, String[] dependencies) {
        Map<String, String> ref = map.computeIfAbsent(resourceType, k -> new LinkedHashMap<>());
        if (!ref.containsKey(resourceId)) {
            ref.put(resourceId, reference);
        }
    }

    @Override
    public Map<String, Map<String, String>> getMap() {
        return map;
    }

    @Override
    public List<String> get(String resourceType) {
        Map<String, String> ref = map.get(resourceType);
        if (ref == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(ref.values());
        }
    }

    @Override
    public String get(String resourceType, String resourceId) {
        Map<String, String> ref = map.get(resourceType);
        if (ref == null) {
            return null;
        } else {
            return ref.get(resourceId);
        }
    }
}
