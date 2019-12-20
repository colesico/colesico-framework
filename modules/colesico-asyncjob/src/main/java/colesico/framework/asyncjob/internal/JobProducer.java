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

import colesico.framework.asyncjob.JobDao;
import colesico.framework.asyncjob.JobEnqueuer;
import colesico.framework.asyncjob.JobService;
import colesico.framework.asyncjob.PayloadConverter;
import colesico.framework.asyncjob.assist.EventBusJobConsumer;
import colesico.framework.asyncjob.dao.PostgreJobDao;
import colesico.framework.asyncjob.gson.GsonPayloadConverter;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

import javax.inject.Singleton;

import static colesico.framework.ioc.Rank.RANK_MINOR;

@Producer(RANK_MINOR)
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
