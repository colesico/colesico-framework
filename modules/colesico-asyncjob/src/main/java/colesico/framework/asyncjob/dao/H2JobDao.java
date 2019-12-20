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
