/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.test.example.weblet;

import colesico.framework.example.weblet.HelloWeblet;
import colesico.framework.httpserver.HttpServer;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.conditional.TestCondition;
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
        TestCondition.enable();
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
    public void testHello() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8085/hello-weblet/say-hello")).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        IO.println("Response: "+response);
        assertEquals(response.body(), HelloWeblet.SAY_HELLO_TEXT);
    }

    @Test
    public void testPrivet() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8085/hello-weblet/privet?name=Таня")).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), HelloWeblet.SAY_PRIVET_TEXT + "Таня");
    }
}
