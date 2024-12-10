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

package colesico.framework.task.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.task.TaskDispatcher;
import colesico.framework.task.TaskExecutor;
import colesico.framework.task.TaskScheduler;
import colesico.framework.task.TaskVTExecutor;
import colesico.framework.task.registry.DefaultTaskRegistry;
import colesico.framework.task.registry.TaskRegistry;

@Producer
@Produce(value = DefaultTaskRegistry.class, keyType = TaskRegistry.class)
@Produce(value = TaskDispatcherImpl.class, keyType = TaskDispatcher.class)
@Produce(value = TaskExecutorImpl.class, keyType = TaskExecutor.class)
@Produce(value = TaskVTExecutorImpl.class, keyType = TaskVTExecutor.class)
@Produce(value = TaskSchedulerImpl.class, keyType = TaskScheduler.class)
public class TaskProducer {

}