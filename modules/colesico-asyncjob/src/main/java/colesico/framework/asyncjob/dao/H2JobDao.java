package colesico.framework.asyncjob.dao;

import colesico.framework.asyncjob.JobDao;
import colesico.framework.asyncjob.JobQueueConfigPrototype;
import colesico.framework.asyncjob.JobRecord;
import colesico.framework.asyncjob.JobServiceConfigPrototype;

import java.time.Duration;

//TODO: H2 dao for tests
public class H2JobDao implements JobDao {

    private final JobServiceConfigPrototype srvConfig;

    public H2JobDao(JobServiceConfigPrototype srvConfig) {
        this.srvConfig = srvConfig;
    }

    @Override
    public Long enqueue(JobQueueConfigPrototype queueConfig, String payload, Duration delay) {
        return null;
    }

    @Override
    public JobRecord pick(JobQueueConfigPrototype queueConfig) {
        return null;
    }
}
