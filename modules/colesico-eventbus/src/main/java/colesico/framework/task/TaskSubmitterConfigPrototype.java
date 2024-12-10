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

package colesico.framework.task;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

/**
 * Task queue configuration prototype
 */
@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class TaskSubmitterConfigPrototype extends TaskExecutorConfigPrototype {

    /**
     * 0 - for unlimited
     */
    public int getQueueCapacity() {
        return 500;
    }

    /**
     * Maximum number of active task workers
     */
    public int getMaximumPoolSize() {
        return 1;
    }

    public long getKeepAliveTime() {
        return 0L;
    }

}
