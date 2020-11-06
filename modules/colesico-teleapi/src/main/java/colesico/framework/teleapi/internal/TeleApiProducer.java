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

package colesico.framework.teleapi.internal;

import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleException;

/**
 * Default tele api producer
 */
@Producer
public class TeleApiProducer {
    protected static final String NO_DATA_PORT_MSG = "TeleAPI: Data port for the active thread is not provided";

    /**
     * Produces data port
     */
    @Unscoped
    public DataPort getDataPort(ThreadScope scope) {
        DataPort port = scope.get(DataPort.SCOPE_KEY);
        if (port == null) {
            throw new TeleException(NO_DATA_PORT_MSG);
        }
        return port;
    }
}
