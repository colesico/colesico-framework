package colesico.framework.asyncjob.internal;

import colesico.framework.asyncjob.JobQueueConfigPrototype;

public final class QueueRef {

    private final JobQueueConfigPrototype config;

    /**
     * Time in milliseconds, during  that  the queue can not be captured by the worker
     */
    private volatile long capturedTill = 0;

    public QueueRef(JobQueueConfigPrototype config) {
        this.config = config;
    }

    public JobQueueConfigPrototype getConfig() {
        return config;
    }

    public long getCapturedTill() {
        return capturedTill;
    }

    public void setCapturedTill(long capturedTill) {
        this.capturedTill = capturedTill;
    }
}
