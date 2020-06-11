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

package colesico.framework.weblet.teleapi;

import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;
import colesico.framework.weblet.Origin;

/**
 * Weblet tele-data reading context
 *
 * @author Vladlen Larionov
 */
public class WebletTDRContext {
    private final String name;
    private final OriginFacade originFacade;

    public WebletTDRContext(String name, OriginFacade originFacade) {
        this.name = name;
        this.originFacade = originFacade;
    }

    /**
     * Parameter name
     */
    public final String getName() {
        return name;
    }

    /**
     * Parameter value
     */
    public final String getString(RouterContext routerContext, HttpRequest httpRequest) {
        return originFacade.getString(name, routerContext, httpRequest);
    }

    /**
     * Parameter origin
     *
     * @see Origin
     */
    public final Origin getOrigin() {
        return originFacade.getOrigin();
    }
}
