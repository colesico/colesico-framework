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
package colesico.framework.undertow;


import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;

/**
 * @author Vladlen Larionov
 */
@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class UndertowConfigPrototype {
    /**
     * Use this method to setup undertow builder options
     *
     * @param builder
     */
    abstract public void applyOptions(Undertow.Builder builder);

    /**
     * This option allow to set custom root http handler.
     */
    public HttpHandler getRootHandler(HttpHandler nextHandler) {
        return nextHandler; // use default root handler
    }

    /**
     * Enable this to allow dumping http response body.
     * Enabling this option is useful for responses debugging.
     * Using this in a production will cause a slight performance degradation.
     */
    public boolean enableStoredResponses() {
        return false;
    }

    public int getMaxIndividualFileSize() {
        return 1024 * 1024 * 10; // 10 MB
    }
}
