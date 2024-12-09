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

package colesico.framework.test.example.asynctask;


import colesico.framework.eventbus.EventService;
import colesico.framework.example.asynctask.TasksSubmitterService;
import colesico.framework.example.asynctask.eventbus.TaskListenerService;
import colesico.framework.example.asynctask.performer.TaskPerformerService;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AsyncTaskExampleTest {
    private Ioc ioc;
    private EventService taskService;

    @BeforeClass
    public void init() {
        ioc = IocBuilder.create().build();
        taskService = ioc.instance(EventService.class);
        taskService.start();
    }

    @AfterClass
    public void done() {
        taskService.stop();
    }

    @Test
    public void testEventBus() {

        // Enqueue task
        TasksSubmitterService publisherService = ioc.instance(TasksSubmitterService.class);
        publisherService.enqueueTasks();

        // Await some time
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TaskListenerService consumerService = ioc.instance(TaskListenerService.class);

        assertEquals(consumerService.payload.value, "value");

        TaskPerformerService performerService = ioc.instance(TaskPerformerService.class);
        assertEquals(performerService.payload.value, "perf");

    }


}
