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

package colesico.framework.asynctask.internal;

import colesico.framework.asynctask.TaskQueueConfigPrototype;
import colesico.framework.asynctask.TaskService;
import colesico.framework.ioc.production.Polysupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class TaskServiceImpl implements TaskService {

    private final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final Polysupplier<TaskQueueConfigPrototype> queueConfigs;
    private final DefaultConsumer defaultConsumer;

    private final Map<Class<?>, TaskQueue> queues = new HashMap<>();

    private volatile boolean running = false;

    public TaskServiceImpl(Polysupplier<TaskQueueConfigPrototype> queueConfigs, DefaultConsumer defaultConsumer) {
        this.queueConfigs = queueConfigs;
        this.defaultConsumer = defaultConsumer;
    }

    @Override
    public synchronized void start() {
        logger.debug("Starting async tasks service...");
        if (running) {
            throw new IllegalStateException("Tasks service is already started");
        }

        queues.clear();
        queueConfigs.forEach(queueConfig -> {
            logger.debug("Found task queue definition for payload type '{}'", queueConfig.getPayloadType());

            TaskQueue taskQueue = new TaskQueue(queueConfig, defaultConsumer);
            TaskQueue oldQueue = queues.put(queueConfig.getPayloadType(), taskQueue);
            if (oldQueue != null) {
                throw new RuntimeException("Duplicate task payload type '" + queueConfig.getPayloadType() + "' defined in " + queueConfig);
            }

        }, null);

        running = true;
        logger.debug("Async tasks service has been started");
    }

    @Override
    public synchronized void stop() {
        checkRunning();
        // Stop
        for (TaskQueue queue : queues.values()) {
            queue.stop();
        }

        // Await termination
        for (TaskQueue queue : queues.values()) {
            queue.awaitTermination(60);
        }

        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void checkRunning() {
        if (!running) {
            throw new IllegalStateException("Background tasks service is not started");
        }
    }

    @Override
    public <P> void enqueue(P taskPayload) {
        checkRunning();
        final TaskQueue queue = queues.get(taskPayload.getClass());
        if (queue == null) {
            throw new RuntimeException("Undetermined task payload: " + taskPayload);
        }
        queue.enqueue(taskPayload);
    }

}
