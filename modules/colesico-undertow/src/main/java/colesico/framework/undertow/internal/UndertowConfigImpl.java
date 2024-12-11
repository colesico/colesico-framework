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
package colesico.framework.undertow.internal;


import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.undertow.UndertowConfigPrototype;
import io.undertow.Undertow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * Default config internal
 *
 * @author Vladlen Larionov
 */

@Singleton
@Substitute(Substitution.STUB)
public final class UndertowConfigImpl extends UndertowConfigPrototype {
    private static Logger log = LoggerFactory.getLogger(UndertowConfigImpl.class);

    @Override
    public void applyOptions(Undertow.Builder builder) {
        builder.addHttpListener(8080, "localhost");
        log.info("Set default undertow http listener to localhost:8080");
    }
}
