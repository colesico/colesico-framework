package colesico.framework.asyncjob.internal;

import colesico.framework.asyncjob.JobQueueConfig;

public final class QueueRef {

    private final JobQueueConfig config;

    /**
     * Time in milliseconds, during  that  the queue can not be captured by the worker
     */
    private volatile long capturedTill = 0;

    public QueueRef(JobQueueConfig config) {
        this.config = config;
    }

    public JobQueueConfig getConfig() {
        return config;
    }

    public long getCapturedTill() {
        return capturedTill;
    }

    public void setCapturedTill(long capturedTill) {
        this.capturedTill = capturedTill;
    }
}
