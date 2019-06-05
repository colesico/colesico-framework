package colesico.framework.asyncjob.dao;

import colesico.framework.asyncjob.JobDao;
import colesico.framework.asyncjob.JobQueueConfig;
import colesico.framework.asyncjob.JobRecord;
import colesico.framework.asyncjob.JobServiceConfig;

import java.time.Duration;

//TODO: H2 dao for tests
public class H2JobDao implements JobDao {

    private final JobServiceConfig srvConfig;

    public H2JobDao(JobServiceConfig srvConfig) {
        this.srvConfig = srvConfig;
    }

    @Override
    public Long enqueue(JobQueueConfig queueConfig, String payload, Duration delay) {
        return null;
    }

    @Override
    public JobRecord pick(JobQueueConfig queueConfig) {
        return null;
    }
}
