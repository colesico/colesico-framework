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
package colesico.framework.undertow.internal;


import colesico.framework.undertow.UndertowConfig;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import org.xnio.Options;

import javax.inject.Singleton;

/**
 * Default config internal
 *
 * @author Vladlen Larionov
 */

@Singleton
public final class UndertowConfigImpl extends UndertowConfig {

    @Override
    public void applyOptions(Undertow.Builder builder) {
        builder.addHttpListener(8080, "localhost");

        /*
        // maximum number of query parameters
        builder.setServerOption(UndertowOptions.MAX_PARAMETERS, 100);
        // maximum number of headers
        builder.setServerOption(UndertowOptions.MAX_HEADERS, 25);
        */
    }
}
