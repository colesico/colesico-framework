package colesico.framework.asyncjob.internal;

import colesico.framework.ioc.ThreadScope;
import colesico.framework.asyncjob.*;
import colesico.framework.transaction.TransactionalShell;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs queues processing
 */
public final class Worker implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(Worker.class);

    private final JobServiceConfigPrototype srvConfig;
    private final JobDao jobsDao;
    private final PayloadConverter payloadTransformer;
    private final QueuePool queuePool;
    private final ThreadScope threadScope;
    private final Object workersMonitor;

    private String workerThreadName;

    public Worker(JobServiceConfigPrototype srvConfig, JobDao jobsDao, PayloadConverter payloadTransformer, QueuePool queuePool, ThreadScope threadScope, Object workersMonitor) {
        this.srvConfig = srvConfig;
        this.jobsDao = jobsDao;
        this.payloadTransformer = payloadTransformer;
        this.queuePool = queuePool;
        this.threadScope = threadScope;
        this.workersMonitor = workersMonitor;
    }

    private void idle() {
        try {
            synchronized (workersMonitor) {
                workersMonitor.wait(srvConfig.getWorkerIdleTimeoutMs());
            }
        } catch (InterruptedException e) {
            logger.warn("Job queue worker process has interrupted");
        }
    }

    private Object transformPayload(JobQueueConfigPrototype queueConfig, JobRecord jobRecord) {
        return payloadTransformer.toObject(queueConfig.getPayloadType(), jobRecord.getPayload());
    }

    private void notifyWorker() {
        synchronized (workersMonitor) {
            workersMonitor.notify();
        }
    }

    private void processQueue(final QueueRef queueRef) {
        final boolean isDebugEnabled = logger.isDebugEnabled();
        if (isDebugEnabled) {
            logger.debug("Process queue " + queueRef.getConfig() + " in thread " + workerThreadName);
        }
        try {
            final TransactionalShell txShell = srvConfig.getTransactionalShell();
            txShell.requiresNew(() -> {
                final JobQueueConfigPrototype queueConfig = queueRef.getConfig();

                // Get job info from DB
                final JobRecord jobRec = jobsDao.pick(queueConfig);
                if (jobRec == null) {
                    if (isDebugEnabled) {
                        logger.debug("Queue is empty: " + queueRef.getConfig());
                    }
                    return null;
                } else {

                    // Release queue for next job processing
                    queuePool.release(queueRef);
                    notifyWorker();
                }

                // Deserialize payload and call job handler
                final Object payloadObj = transformPayload(queueConfig, jobRec);
                try {
                    if (isDebugEnabled) {
                        logger.debug("Call job consumer: " + queueRef.getConfig() + " with payload: " + jobRec.getPayload());
                    }
                    queueConfig.getJobConsumer().consume(payloadObj);
                } catch (Exception e) {
                    logger.error("Call job consumer error: " + ExceptionUtils.getRootCauseMessage(e), e);
                }

                return jobRec;
            });
        } catch (Exception e) {
            logger.error("Error processing job {}. Message: {}" + jobsDao, ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    @Override
    public void run() {
        workerThreadName = Thread.currentThread().getName();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Capture queue to be processed
                final QueueRef queueRef = queuePool.capture(srvConfig.getQueueCaptureTimeoutMs());
                if (queueRef == null) {
                    idle();
                } else {
                    processQueue(queueRef);
                }
            } finally {
                threadScope.destroy();
            }
        }
    }
}
