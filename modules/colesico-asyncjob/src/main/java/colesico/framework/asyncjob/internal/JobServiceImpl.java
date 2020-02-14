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

package colesico.framework.asyncjob.internal;

import colesico.framework.asyncjob.*;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.transaction.TransactionalShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
public class JobServiceImpl implements JobService {

    private final Logger logger = LoggerFactory.getLogger(JobService.class);

    private final JobServiceConfigPrototype srvConfig;
    private final JobDao jobsDao;
    private final PayloadConverter payloadTransformer;
    private final Polysupplier<JobQueueConfigPrototype> queueConfigs;
    private final ThreadScope threadScope;

    private QueuePool queuePool;
    private Thread[] workersThreads;
    private final Map<Class<?>, QueueRef> queueRefs = new HashMap<>();

    private final Object workersMonitor = new Object();

    private volatile boolean running = false;

    public JobServiceImpl(JobServiceConfigPrototype srvConfig, JobDao jobsDao, PayloadConverter payloadTransformer, Polysupplier<JobQueueConfigPrototype> queueConfigs, ThreadScope threadScope) {
        this.srvConfig = srvConfig;
        this.jobsDao = jobsDao;
        this.payloadTransformer = payloadTransformer;
        this.queueConfigs = queueConfigs;
        this.threadScope = threadScope;
    }

    @Override
    public synchronized void start() {
        logger.debug("Starting jobs service...");
        if (running) {
            throw new IllegalStateException("Jobs service is already started");
        }

        final Set<String> queueTables = new HashSet<>();
        queueRefs.clear();
        queueConfigs.forEach(queueConfig -> {
            logger.debug("Found job queue definition for payload type '{}' on table '{}'", queueConfig.getPayloadType(), queueConfig.getTableName());

            if (queueTables.contains(queueConfig.getTableName())) {
                throw new RuntimeException("Duplicate job queue table '" + queueConfig.getTableName() + "' defined in " + queueConfig);
            }
            queueTables.add(queueConfig.getTableName());

            QueueRef oldRef = queueRefs.put(queueConfig.getPayloadType(), new QueueRef(queueConfig));
            if (oldRef != null) {
                throw new RuntimeException("Duplicate job payload type '" + queueConfig.getPayloadType() + "' defined in " + queueConfig);
            }

        }, null);

        queuePool = new QueuePool(queueRefs.values().toArray(new QueueRef[queueRefs.size()]));

        workersThreads = new Thread[srvConfig.getWorkersNum()];
        for (int i = 0; i < workersThreads.length; i++) {
            Worker worker = new Worker(srvConfig, jobsDao, payloadTransformer, queuePool, threadScope, workersMonitor);
            String threadName = "JOB_QUEUE_WORKER_" + i;
            Thread thread = new Thread(worker, threadName);
            workersThreads[i] = thread;
            thread.start();
            logger.debug("Job queue worker has started in thread " + threadName);
        }

        running = true;
        logger.debug("Jobs service has been started");
    }

    @Override
    public synchronized void stop() {
        checkRunning();
        for (Thread t : workersThreads) {
            t.interrupt();
        }
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void checkRunning() {
        if (!running) {
            throw new IllegalStateException("Background jobs service is not started");
        }
    }

    private void notifyWorker() {
        synchronized (workersMonitor) {
            workersMonitor.notify();
        }
    }

    @Override
    public <P> void enqueue(P jobPayload, Duration delay) {
        checkRunning();
        final QueueRef queueRef = queueRefs.get(jobPayload.getClass());
        if (queueRef == null) {
            throw new RuntimeException("Job queue not found for payload type: " + jobPayload.getClass());
        }

        final String payloadStr = payloadTransformer.fromObject(jobPayload);
        final TransactionalShell txShell = srvConfig.getTransactionalShell();

        txShell.requiresNew(() -> {
            JobQueueConfigPrototype queueConfig = queueRef.getConfig();
            jobsDao.enqueue(queueConfig, payloadStr, delay);
            return null;
        });

        queuePool.release(queueRef);
        notifyWorker();
    }

}
