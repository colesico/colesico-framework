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

package colesico.framework.test.example.helloworld;

import colesico.framework.example.helloworld.HelloWeblet;
import colesico.framework.httpserver.HttpServer;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.testng.Assert.assertEquals;

public class HelloWorldTest {
    private Ioc ioc;
    private HttpServer httpServer;
    private HttpClient httpClient;
    private Logger logger = LoggerFactory.getLogger(HelloWorldTest.class);

    @BeforeClass
    public void init() {
        ioc = IocBuilder.create().build();

        httpServer = ioc.instance(HttpServer.class).start();

        httpClient = HttpClient.newHttpClient();
        logger.info("Ready for hello world tests");
    }

    @AfterClass
    public void destroy() {
        httpServer.stop();
    }

    @Test
    public void test1() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8085/hello-weblet/say-hello")).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HelloWeblet.SAY_HELLO_TEXT, response.body());
    }

    @Test
    public void test2() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8085/hello-weblet/holla")).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HelloWeblet.SAY_HOLLA_TEXT, response.body());
    }
}
