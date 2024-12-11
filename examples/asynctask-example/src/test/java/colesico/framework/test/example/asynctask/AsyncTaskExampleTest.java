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


import colesico.framework.asynctask.TaskDispatcher;
import colesico.framework.asynctask.TaskExecutor;
import colesico.framework.example.asynctask.AsyncProducerService;
import colesico.framework.example.asynctask.Consumer;
import colesico.framework.example.asynctask.SyncProducerService;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncTaskExampleTest {
    private Ioc ioc;

    private Consumer consumer;
    private SyncProducerService syncProducer;
    private AsyncProducerService asyncProducer;

    private TaskDispatcher dispatcher;
    private TaskExecutor executor;

    @BeforeClass
    public void init() {
        ioc = IocBuilder.create().build();
        consumer = ioc.instance(Consumer.class);
        syncProducer = ioc.instance(SyncProducerService.class);
        asyncProducer = ioc.instance(AsyncProducerService.class);

        dispatcher = ioc.instance(TaskDispatcher.class);
        executor = ioc.instance(TaskExecutor.class);
        executor.start();
    }

    @AfterClass
    public void done() {
        executor.stop();
    }

    @Test
    public void testAyncTask() throws ExecutionException, InterruptedException {


        Collection<Future<String>> res = asyncProducer.produceTasks();
        // Await some time
        Thread.sleep(100);
        String task1Result = res.iterator().next().get();
        System.out.println(task1Result);

    }


}
