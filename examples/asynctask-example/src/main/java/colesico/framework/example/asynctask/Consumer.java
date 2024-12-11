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

package colesico.framework.example.asynctask;

import colesico.framework.asynctask.OnTask;
import colesico.framework.service.Service;

@Service
public class Consumer {

    private Task1 task1;
    private Task1 task2;
    private Task2 task3;
    private Task2 task4;

    @OnTask
    public String worker1(Task1 task) {
        System.out.println("Worker1: " + task.message());
        this.task1 = task;
        return "Hello from Worker1";
    }

    @OnTask
    public void worker2(Task1 task) {
        System.out.println("Worker2: " + task.message());
        this.task2 = task;
    }

    @OnTask
    public String worker3(Task2 task) {
        System.out.println("Worker3: " + task.message());
        this.task3 = task;
        return "Hello from Worker3";
    }

    @OnTask
    public void worker4(Task2 task) {
        System.out.println("Worker4: " + task.message());
        this.task4 = task;
    }

    public Task1 getTask1() {
        return task1;
    }

    public Task1 getTask2() {
        return task2;
    }

    public Task2 getTask3() {
        return task3;
    }

    public Task2 getTask4() {
        return task4;
    }

    @Override
    public String toString() {
        return "Consumer{" +
                "task1=" + task1 +
                ", task2=" + task2 +
                ", task3=" + task3 +
                ", task4=" + task4 +
                '}';
    }
}
