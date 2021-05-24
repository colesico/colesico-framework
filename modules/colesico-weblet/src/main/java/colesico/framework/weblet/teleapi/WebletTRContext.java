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

import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.Origin;

/**
 * Weblet tele-reading context
 *
 * @author Vladlen Larionov
 */
public final class WebletTRContext extends HttpTRContext {

    /**
     * Custom reader class or null.
     * If null - default reader will be used to read the parameter
     */
    private final Class<? extends WebletTeleReader> readerClass;

    public WebletTRContext(String paramName, String originName, Class<? extends WebletTeleReader> readerClass) {
        super(paramName, originName);
        this.readerClass = readerClass;
    }

    public WebletTRContext(String paramName, String originName) {
        super(paramName, originName);
        this.readerClass = null;
    }

    public WebletTRContext(String paramName) {
        super(paramName, WebletOrigin.AUTO);
        this.readerClass = null;
    }

    public WebletTRContext() {
        super(null, null);
        this.readerClass = null;
    }

    public Class<? extends WebletTeleReader> getReaderClass() {
        return readerClass;
    }

}
