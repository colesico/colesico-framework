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

package colesico.framework.taskhub.internal;

import colesico.framework.taskhub.EventScheduler;
import colesico.framework.taskhub.EventService;
import colesico.framework.taskhub.EventSubmitter;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

import javax.inject.Singleton;

@Producer
@Produce(EventServiceImpl.class)
@Produce(DefaultTaskPerformer.class)
public class TaskProducer {

    @Singleton
    public EventService getTaskService(EventServiceImpl impl) {
        return impl;
    }

    @Singleton
    public EventSubmitter getTaskPublisher(EventServiceImpl impl) {
        return impl;
    }

    @Singleton
    public EventScheduler getTaskScheduler(EventServiceImpl impl) {
        return impl;
    }

}