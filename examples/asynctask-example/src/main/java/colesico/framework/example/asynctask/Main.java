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

import colesico.framework.asynctask.TaskService;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;

public class Main {
    public static void main(String[] args) {
        Ioc ioc = IocBuilder.create().build();

        // Start async tasks service
        TaskService taskService = ioc.instance(TaskService.class);
        taskService.start();

        // Enqueue task
        TaskPublisherService publisherService = ioc.instance(TaskPublisherService.class);
        publisherService.enqueueTask();

        // Await some time
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop task service
        taskService.stop();

        // Print task
        TaskConsumerService consumerService = ioc.instance(TaskConsumerService.class);
        System.out.println(consumerService.task);
    }
}
