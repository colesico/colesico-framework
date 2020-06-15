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

/**
 * Weblet tele-reading context
 *
 * @author Vladlen Larionov
 */
public final class WebletTRContext {

    /**
     * Parameter name
     */
    private final String name;

    /**
     * Origin facade to read parameter from it
     */
    private final OriginFacade originFacade;

    /**
     * Custom reader class or null.
     * If null - default reader will be used to read the parameter
     */
    private final Class<? extends WebletTeleReader> readerClass;

    public WebletTRContext(String name, OriginFacade originFacade, Class<? extends WebletTeleReader> readerClass) {
        this.name = name;
        this.originFacade = originFacade;
        this.readerClass = readerClass;
    }

    public WebletTRContext(String name, OriginFacade originFacade) {
        this.name = name;
        this.originFacade = originFacade;
        this.readerClass = null;
    }

    /**
     * Parameter name
     */
    public final String getName() {
        return name;
    }

    /**
     * Origin facade
     */
    public OriginFacade getOriginFacade() {
        return originFacade;
    }

    public Class<? extends WebletTeleReader> getReaderClass() {
        return readerClass;
    }


    /**
     * Parameter value
     */
    public final String getString(RouterContext routerContext, HttpRequest httpRequest) {
        return originFacade.getString(name, routerContext, httpRequest);
    }
}
