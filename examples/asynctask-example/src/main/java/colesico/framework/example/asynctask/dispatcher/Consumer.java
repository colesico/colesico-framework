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

import colesico.framework.service.Service;
import colesico.framework.asynctask.OnTask;

@Service
public class Consumer {

    private Task1 task1;
    private Task2 task2;

    @OnTask
    public void onTask1(Task1 task) {
        System.out.println("Consumer on Task1: " + task.message);
        this.task1 = task;
    }

    @OnTask
    public void onTask2(Task2 task) {
        System.out.println("Consumer on Task2: " + task.message);
        this.task2 = task;
    }

    public Task1 getTask1() {
        return task1;
    }

    public Task2 getTask2() {
        return task2;
    }
}
