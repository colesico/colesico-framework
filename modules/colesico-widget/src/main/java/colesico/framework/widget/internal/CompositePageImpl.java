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

import colesico.framework.widget.CompositePage;
import colesico.framework.widget.PageFragment;
import colesico.framework.widget.ResourceReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Vladlen Larionov
 */
public class CompositePageImpl implements CompositePage {

    private final ResourceReference resourceReference;
    private Map<String, PageFragment> fragmentsMap;
    private final Stack<Map<String, PageFragment>> fragmentsStack;

    public CompositePageImpl(Object context) {
        resourceReference = new ResourceReferenceImpl();
        fragmentsMap = new HashMap<>();
        fragmentsStack = new Stack<>();
    }

    @Override
    public ResourceReference getResourceReference() {
        return resourceReference;
    }

    @Override
    public void seclude() {
        fragmentsStack.push(fragmentsMap);
        fragmentsMap = new HashMap<>();
    }

    @Override
    public void restore() {
        fragmentsMap = fragmentsStack.pop();
    }

    @Override
    public Map<String, PageFragment> getFragmentsMap() {
        return fragmentsMap;
    }

    @Override
    public void putFragment(String fragmentId, PageFragment fragment) {
        fragmentsMap.put(fragmentId, fragment);
    }

    @Override
    public PageFragment getFragment(String fragmentId) {
       return  fragmentsMap.get(fragmentId);
    }
}
