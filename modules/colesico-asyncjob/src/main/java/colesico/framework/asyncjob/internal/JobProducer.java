package colesico.framework.asyncjob.internal;

import colesico.framework.asyncjob.assist.EventBusJobConsumer;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;
import colesico.framework.asyncjob.JobDao;
import colesico.framework.asyncjob.JobEnqueuer;
import colesico.framework.asyncjob.JobService;
import colesico.framework.asyncjob.PayloadConverter;
import colesico.framework.asyncjob.dao.PostgreJobDao;
import colesico.framework.asyncjob.gson.GsonPayloadConverter;

import javax.inject.Singleton;

@Producer(Rank.RANK_MINOR)
@Produce(JobServiceImpl.class)
@Produce(PostgreJobDao.class)
@Produce(GsonPayloadConverter.class)
@Produce(EventBusJobConsumer.class)
public class JobProducer {

    @Singleton
    public JobService getJobsKit(JobServiceImpl impl) {
        return impl;
    }

    @Singleton
    public JobEnqueuer getJobEnqueuer(JobServiceImpl impl) {
        return impl;
    }

    @Singleton
    public JobDao getJobsDao(PostgreJobDao impl) {
        return impl;
    }

    @Singleton
    public PayloadConverter getPayloadTransformer(GsonPayloadConverter impl) {
        return impl;
    }

}
