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

package colesico.framework.test.example.rpc;


import colesico.framework.example.rpc.api.HelloServiceRemote;
import colesico.framework.httpserver.HttpServer;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.conditional.TestCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class RpcApiTest {
    private Ioc ioc;
    private HttpServer httpServer;
    private Logger logger = LoggerFactory.getLogger(RpcApiTest.class);

    @BeforeClass
    public void init() {
        TestCondition.enable();
        ioc = IocBuilder.create().build();
        logger.info("Start http server");
        httpServer = ioc.instance(HttpServer.class).start();
    }

    @AfterClass
    public void destroy() {
        logger.info("Stop http server");
        httpServer.stop();
    }

    @Test
    public void test1() {
        logger.info("Test DataBean retrieving");
        HelloServiceRemote remoteSrv = ioc.instance(HelloServiceRemote.class);
        String val = remoteSrv.getDataBean().getValue();
        System.out.println("DataBean.value=" + val);
        assertEquals(HelloServiceRemote.HELLO_MESSAGE, val);
    }

}
