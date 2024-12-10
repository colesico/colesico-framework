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

import colesico.framework.task.TaskDispatcher;
import colesico.framework.task.registry.DefaultTaskRegistry;
import colesico.framework.task.registry.TaskRegistry;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

import javax.inject.Singleton;

@Producer
@Produce(value = DefaultTaskRegistry.class, keyType = TaskRegistry.class, scoped = Singleton.class)
@Produce(value = TaskDispatcherImpl.class, keyType = TaskDispatcher.class, scoped = Singleton.class)
public class TaskProducer {

}