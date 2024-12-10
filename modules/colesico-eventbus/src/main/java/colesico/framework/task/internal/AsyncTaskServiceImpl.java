/*Job
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

package colesico.framework.task.internal;

import colesico.framework.task.*;

import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class AsyncTaskServiceImpl implements TaskService, TaskExecutor, TaskScheduler {


    private <P> TaskScheduledExecutor getScheduleExecutor(P taskPayload) {
        checkRunning();
        final TaskScheduledExecutor executor = schedules.get(taskPayload.getClass());
        if (executor == null) {
            throw new RuntimeException("Undetermined task payload: " + taskPayload);
        }
        return executor;
    }

    @Override
    public <P> void schedule(P task, long delay, TimeUnit unit) {
        getScheduleExecutor(task).schedule(task, delay, unit);
    }

    @Override
    public <P> void scheduleAtFixedRate(P task, long initialDelay, long period, TimeUnit unit) {
        getScheduleExecutor(task).scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    @Override
    public <P> void scheduleWithFixedDelay(P task, long initialDelay, long delay, TimeUnit unit) {
        getScheduleExecutor(task).scheduleWithFixedDelay(task, initialDelay, delay, unit);
    }
}
