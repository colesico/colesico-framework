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

import java.lang.reflect.Type;

/**
 * Weblet tele-reading context
 *
 * @author Vladlen Larionov
 */
public final class WebletTRContext extends HttpTRContext {

    public static final String OF_METHOD = "of";

    /**
     * Custom reader class or null.
     * If null - default reader will be used to read the parameter
     */
    private final Class<? extends WebletTeleReader> readerClass;

    private WebletTRContext(Type valueType, String paramName, String originName, Class<? extends WebletTeleReader> readerClass) {
        super(valueType, paramName, originName);
        this.readerClass = readerClass;
    }

    public static WebletTRContext of(Type valueType) {
        return new WebletTRContext(valueType, null, null, null);
    }

    public static WebletTRContext of(Type valueType, String paramName, String originName) {
        return new WebletTRContext(valueType, paramName, originName, null);
    }

    public static WebletTRContext of(Type valueType, String paramName) {
        return new WebletTRContext(valueType, paramName, WebletOrigin.AUTO, null);
    }

    public static WebletTRContext of(Type valueType, String paramName, String originName, Class<? extends WebletTeleReader> readerClass) {
        return new WebletTRContext(valueType, paramName, originName, readerClass);
    }

    public Class<? extends WebletTeleReader> getReaderClass() {
        return readerClass;
    }

}
