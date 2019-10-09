package colesico.framework.asyncjob;

import java.time.Duration;

/**
 * Jobs service dao.
 * Use ioc producer to customize dao implementation.
 */
public interface JobDao {

    /**
     * Put job to queue
     */
    Long enqueue(JobQueueConfigPrototype queueConfig, String payload, Duration delay);

    /**
     * Return next job from queue or null
     *
     * @param queueConfig
     * @return
     */
    JobRecord pick(JobQueueConfigPrototype queueConfig);
}
