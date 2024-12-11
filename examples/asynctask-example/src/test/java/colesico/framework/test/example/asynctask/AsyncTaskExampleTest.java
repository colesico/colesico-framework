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


import colesico.framework.asynctask.TaskExecutor;
import colesico.framework.example.asynctask.AsyncSender;
import colesico.framework.example.asynctask.Consumer;
import colesico.framework.example.asynctask.SyncSender;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.testng.Assert.assertEquals;

public class AsyncTaskExampleTest {
    private Ioc ioc;

    private Consumer consumer;
    private SyncSender syncSender;
    private AsyncSender asyncSender;

    private TaskExecutor executor;

    @BeforeClass
    public void init() {
        ioc = IocBuilder.create().build();
        consumer = ioc.instance(Consumer.class);
        syncSender = ioc.instance(SyncSender.class);
        asyncSender = ioc.instance(AsyncSender.class);

        executor = ioc.instance(TaskExecutor.class);
        executor.start();
    }

    @AfterClass
    public void done() {
        executor.stop();
    }

    @Test
    public void testTask() throws ExecutionException, InterruptedException {

        syncSender.produceTasks();

        Collection<Future<String>> results = asyncSender.produceTasks();
        // Await some time
        Thread.sleep(100);
        for (Future<String> res : results) {
            System.out.println("Worker result: " + res.get());
        }

        System.out.println("Consumer state: " + consumer);

        assertEquals(consumer.getTask1().message(), "Hello1");
        assertEquals(consumer.getTask2().message(), "Hello1");
        assertEquals(consumer.getTask3().message(), "Hello2");
        assertEquals(consumer.getTask4().message(), "Hello2");

    }


}
