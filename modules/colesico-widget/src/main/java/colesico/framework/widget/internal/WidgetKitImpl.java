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

import colesico.framework.ioc.Ioc;
import colesico.framework.widget.CompositePage;
import colesico.framework.widget.RenderResponse;
import colesico.framework.widget.Widget;
import colesico.framework.widget.WidgetKit;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 *
 * @author Vladlen Larionov
 */
@Singleton
public class WidgetKitImpl implements WidgetKit {

    protected final Ioc ioc;
    protected final Provider<CompositePage> compositePageProv;

    @Inject
    public WidgetKitImpl(Ioc ioc, Provider<CompositePage> compositePageProv) {
        this.ioc = ioc;
        this.compositePageProv = compositePageProv;
    }

    /**
     * Render widget
     *
     * @param fragmentId identifier of location of widget contents on the page
     * @param widgetClass service class which renders widget
     * @param widgetId id of widget
     * @param params
     */
    @Override
    public void renderWidget(Class<? extends Widget> widgetClass, String widgetId, String fragmentId, Object... params) {
        Widget Widget = ioc.instance(widgetClass);
        RenderResponse response = new RenderResponseImpl(compositePageProv.get(), fragmentId);
        Widget.renderWidget(widgetId, response, params);
    }

    @Override
    public void renderWidget(String widgetClassName, String widgetId, String fragmentId, Object... params) {
        Class<? extends Widget> widgetClass;
        try {
            widgetClass = (Class<? extends Widget>) Class.forName(widgetClassName);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        renderWidget(widgetClass, widgetId, fragmentId, params);
    }

}
