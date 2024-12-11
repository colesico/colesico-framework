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

package colesico.framework.asynctask.internal;

import colesico.framework.asynctask.TaskDispatcher;
import colesico.framework.asynctask.registry.TaskRegistry;
import colesico.framework.asynctask.registry.TaskWorker;

import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class TaskDispatcherImpl implements TaskDispatcher {

    private final TaskRegistry registry;

    public TaskDispatcherImpl(TaskRegistry registry) {
        this.registry = registry;
    }

    @Override
    public <T> void dispatch(T task) {
        registry.applyVoid(task.getClass(),
                worker -> ((TaskWorker<T, ?>) worker).work(task)
        );
    }

    @Override
    public <T, R> Collection<R> dispatchReturn(final T task) {
        return registry.applyReturn(task.getClass(),
                worker -> ((TaskWorker<T, R>) worker).work(task)
        );
    }

}