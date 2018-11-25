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
package colesico.framework.widget;

import colesico.framework.ioc.Key;
import colesico.framework.ioc.TypeKey;

import java.util.Map;

/**
 * Composite page allows to separately store fragments of html page. Each fragment has an id. Also, the page contains a
 * structure for links to various resources attached to the page (css, js...)
 *
 * @author Vladlen Larionov
 */
public interface CompositePage {

    Key<CompositePage> SCOPE_KEY = new TypeKey<>(CompositePage.class);

    ResourceReference getResourceReference();

    /**
     * Puts fragment to the current fragments model
     *
     * @param fragmentId
     * @param fragment
     */
    void putFragment(String fragmentId, PageFragment fragment);

    PageFragment getFragment(String fragmentId);

    /**
     * Returns the current fragments model.
     *
     * @return
     */
    Map<String, PageFragment> getFragmentsMap();

    /**
     * Creates a new current fragments model. The previous created model is stored in the stack.
     */
    void seclude();

    /**
     * Restores the previous fragments model with the stack.
     */
    void restore();

}
