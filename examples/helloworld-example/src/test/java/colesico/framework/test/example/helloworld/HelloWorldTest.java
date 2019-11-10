/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.test.example.helloworld;

import colesico.framework.example.helloworld.HelloWeblet;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.undertow.HttpServer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.*;

public class HelloWorldTest {
    private Ioc ioc;
    private HttpServer httpServer;
    private HttpClient httpClient;
    private Logger logger = LoggerFactory.getLogger(HelloWorldTest.class);

    @BeforeClass
    public void init() {
        ioc = IocBuilder.forTests();

        httpServer = ioc.instance(HttpServer.class).start();

        httpClient = HttpClientBuilder.create().build();
        logger.info("Ready for hello world tests");
    }

    @AfterClass
    public void destroy() {
        httpServer.stop();
    }

    @Test
    public void test1() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:8085/hello-weblet/say-hello");
        String responseText = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        assertEquals(HelloWeblet.SAY_HELLO_TEXT, responseText);
    }

    @Test
    public void test2() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:8085/hello-weblet/holla");
        String responseText = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        assertEquals(HelloWeblet.SAY_HOLLA_TEXT, responseText);
    }
}
