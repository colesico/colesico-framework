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

package colesico.framework.test.example.web;


import colesico.framework.httpserver.HttpServer;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.conditional.TestCondition;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.http.HttpClient;

public class WebExampleTest {
    private Ioc ioc;
    private HttpServer httpServer;
    private HttpClient httpClient;

    @BeforeClass
    public void init() {
        TestCondition.enable();
        ioc = IocBuilder.create().build();
        httpServer = ioc.instance(HttpServer.class).start();
        httpClient = HttpClient.newBuilder().build();
    }

    @AfterClass
    public void destroy() {
        httpServer.stop();
    }


    @Test
    public void testList() throws Exception {

    }
}
