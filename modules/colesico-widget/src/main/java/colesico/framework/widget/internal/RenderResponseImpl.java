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
package colesico.framework.widget.internal;

import colesico.framework.widget.CompositePage;
import colesico.framework.widget.PageFragment;
import colesico.framework.widget.RenderResponse;

/**
 *
 * @author Vladlen Larionov
 */
public class RenderResponseImpl implements RenderResponse {

    /**
     * Identifies the Location of widget's staticres on the sheet
     */
    protected final String fragmentId;
    protected final CompositePage compositePage;

    public RenderResponseImpl(CompositePage compositePage, String fragmentId) {
        this.compositePage = compositePage;
        this.fragmentId = fragmentId;
    }

    @Override
    public void setContent(String widgetContent) {
        PageFragment pageFragment = new PageFragmentImpl();
        pageFragment.setContent(widgetContent);
        compositePage.putFragment(fragmentId, pageFragment);
    }

    @Override
    public void addResourceReference(String resourceType, String resourceId, String reference, String[] dependencies) {
        compositePage.getResourceReference().add(resourceType, resourceId, reference, dependencies);
    }
}
