/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.teleapi;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.TRContext;
import colesico.framework.teleapi.dataport.TWContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controls tele-methods invocation process considering invocation environment (protocol etc.),
 * performs error handling, etc.
 *
 * @param <P> Protocol context API (request-response)
 */
abstract public class TeleController<P,
        R extends TRContext,
        W extends TWContext,
        F extends TeleFacade<?, ?>,
        M extends TeleMethod<R, W>> {

    protected final Logger log = LoggerFactory.getLogger(TeleController.class);

    protected final ThreadScope threadScope;

    protected final TeleResolver<P, F, M> resolver;

    public TeleController(ThreadScope threadScope, TeleResolver<P, F, M> resolver) {
        this.threadScope = threadScope;
        this.resolver = resolver;
    }

    abstract DataPort<R, W> createDataPort(P protocolContext);

    protected void invoke(M teleMethod, DataPort<R, W> dataPort) {
        teleMethod.invoke(dataPort);
    }

    protected void handleError(Throwable throwable, M teleMethod, DataPort<R, W> dataPort) {
        log.error("Tele-invocation error: '{}'; tele-method: '{}'", throwable.getMessage(), teleMethod);
        dataPort.write(throwable, Throwable.class);
    }

    public void invoke(P protocolContext) {

        var dataPort = createDataPort(protocolContext);
        threadScope.put(DataPort.SCOPE_KEY, dataPort);

        M teleMethod = null;
        try {
            teleMethod = resolver.resolve(protocolContext);
            invoke(teleMethod, dataPort);
        } catch (Throwable t) {
            handleError(t, teleMethod, dataPort);
        } finally {
            threadScope.remove(DataPort.SCOPE_KEY);
        }

    }
}