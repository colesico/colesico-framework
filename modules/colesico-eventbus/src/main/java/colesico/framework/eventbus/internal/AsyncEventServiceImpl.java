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

package colesico.framework.eventbus.internal;

import colesico.framework.eventbus.*;
import colesico.framework.ioc.production.Polysupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Singleton
public class AsyncEventServiceImpl implements EventService, AsyncEventBus, EventScheduler {

    private final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final Polysupplier<EventSubmitterConfigPrototype> queueConfigs;
    private final Polysupplier<EventSchedulerConfigPrototype> scheduleConfigs;

    private final Map<Class<?>, AsyncEventBusImpl> queues = new HashMap<>();
    private final Map<Class<?>, EventScheduledExecutor> schedules = new HashMap<>();

    private volatile boolean running = false;

    public AsyncEventServiceImpl(Polysupplier<EventSubmitterConfigPrototype> queueConfigs,
                                 Polysupplier<EventSchedulerConfigPrototype> scheduleConfigs,
                                 ) {

        this.queueConfigs = queueConfigs;
        this.scheduleConfigs = scheduleConfigs;
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

            AsyncEventBusImpl taskExecutor = new AsyncEventBusImpl(defaultConsumer, queueConfig);
            EventExecutor prev = queues.put(queueConfig.getPayloadType(), taskExecutor);
            if (prev != null) {
                throw new RuntimeException("Duplicate task payload type '" + queueConfig.getPayloadType() + "' defined in " + queueConfig);
            }

        }, null);

        schedules.clear();
        scheduleConfigs.forEach(queueConfig -> {
            logger.debug("Found task schedule definition for payload type '{}'", queueConfig.getPayloadType());

            EventScheduledExecutor taskExecutor = new EventScheduledExecutor(defaultConsumer, queueConfig);
            EventExecutor prev = schedules.put(queueConfig.getPayloadType(), taskExecutor);
            if (prev != null) {
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
        for (AsyncEventBusImpl queue : queues.values()) {
            queue.shutdown();
        }

        // Await termination
        for (AsyncEventBusImpl queue : queues.values()) {
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
            throw new IllegalStateException("Async tasks service is not started");
        }
    }

    @Override
    public <P> void submit(P event) {
        checkRunning();
        final AsyncEventBusImpl executor = queues.get(event.getClass());
        if (executor == null) {
            throw new RuntimeException("Undetermined task payload: " + event);
        }
        executor.submit(event);
    }

    private <P> EventScheduledExecutor getScheduleExecutor(P taskPayload) {
        checkRunning();
        final EventScheduledExecutor executor = schedules.get(taskPayload.getClass());
        if (executor == null) {
            throw new RuntimeException("Undetermined task payload: " + taskPayload);
        }
        return executor;
    }

    @Override
    public <P> void schedule(P event, long delay, TimeUnit unit) {
        getScheduleExecutor(event).schedule(event, delay, unit);
    }

    @Override
    public <P> void scheduleAtFixedRate(P event, long initialDelay, long period, TimeUnit unit) {
        getScheduleExecutor(event).scheduleAtFixedRate(event, initialDelay, period, unit);
    }

    @Override
    public <P> void scheduleWithFixedDelay(P event, long initialDelay, long delay, TimeUnit unit) {
        getScheduleExecutor(event).scheduleWithFixedDelay(event, initialDelay, delay, unit);
    }
}
