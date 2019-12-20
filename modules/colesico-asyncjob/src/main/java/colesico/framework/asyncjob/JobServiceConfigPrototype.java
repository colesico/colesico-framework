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

package colesico.framework.asyncjob;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.transaction.TransactionalShell;

import java.sql.Connection;
import java.time.Duration;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class JobServiceConfigPrototype {

    public static final Long DEFAULT_IDLE_TIMEOUT = Duration.ofSeconds(20).toMillis();
    public static final Long DEFAULT_CAPTURE_TIMEOUT = Duration.ofSeconds(60).toMillis();

    /**
     * Return transactional connection to database
     *
     * @return
     */
    abstract public Connection getConnection();

    abstract public TransactionalShell getTransactionalShell();

    /**
     * Return number of workers to process queues
     *
     * @return
     */
    public int getWorkersNum() {
        return 2;
    }

    /**
     * The delay interval before re-checking the appearance of jobs in the queues in milliseconds
     *
     * @return
     */
    public long getWorkerIdleTimeoutMs() {
        return DEFAULT_IDLE_TIMEOUT;
    }

    public long getQueueCaptureTimeoutMs() {
        return DEFAULT_CAPTURE_TIMEOUT;
    }

}
