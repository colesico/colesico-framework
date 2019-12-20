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

package colesico.framework.test.example.routing;

import colesico.framework.httpserver.HttpServer;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class RoutingTest {
    private Ioc ioc;
    private HttpServer httpServer;
    private HttpClient httpClient;

    @BeforeClass
    public void init() {
        ioc = IocBuilder.forTests();
        httpServer = ioc.instance(HttpServer.class).start();
        httpClient = HttpClientBuilder.create().build();
    }

    @AfterClass
    public void destroy() {
        httpServer.stop();
    }

    @Test
    public void testDefaultRouting() throws Exception{
        var request = new HttpGet("http://localhost:8080/default-routing/hello");
        String response = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        assertEquals(response,"Hello");
    }

    @Test
    public void testAbsoluteRouting() throws Exception{
        var request = new HttpGet("http://localhost:8080/say-hi.html");
        String response = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        assertEquals(response,"Hi");

        request = new HttpGet("http://localhost:8080/absolute-route/say-hello.html");
        response = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        assertEquals(response,"Hello");
    }

    @Test
    public void testIndexOtherRouting() throws Exception {
        var request = new HttpGet("http://localhost:8080/");
        String response = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        assertEquals(response, "Index");

        request = new HttpGet("http://localhost:8080/bla-bla");
        response = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        assertEquals(response,"Other");
    }

    @Test
    public void testRelativeRouting() throws Exception {
        var request = new HttpGet("http://localhost:8080/relative-routing/say-hola");
        String response = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        assertEquals(response, "Hola");
    }

    @Test
    public void testPkgRelRouting() throws Exception {
        var request = new HttpGet("http://localhost:8080/api/v1.0/relative/say-hallo");
        String response = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        assertEquals(response, "Hallo");

        request = new HttpGet("http://localhost:8080/api/v1.0/relative/say-hei");
        response = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        assertEquals(response,"Hei");
    }

    @Test
    public void testSubmitRouting() throws Exception {
        var request = new HttpPost("http://localhost:8080/my-form/submit");
        String response = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        assertEquals(response, "Submit");
    }
}
