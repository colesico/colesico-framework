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

package colesico.framework.example.asynctask.dispatcher;

import colesico.framework.asynctask.TaskDispatcher;
import colesico.framework.asynctask.OnTask;
import colesico.framework.service.Service;

import javax.inject.Inject;

@Service
public class Producer {

    final TaskDispatcher dispatcher;

    @Inject
    public Producer(TaskDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void produceTasks() {
        dispatcher.dispatchReturn(new Task1("Hello1"));
        dispatcher.dispatchReturn(new Task2("Hello2"));
    }

    @OnTask
    public void onTask2(Task2 task) {
        System.out.println("Producer on Task2: " + task.message);
    }
}
