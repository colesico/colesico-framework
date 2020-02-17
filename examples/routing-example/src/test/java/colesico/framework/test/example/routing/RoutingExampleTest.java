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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.testng.Assert.assertEquals;

public class RoutingExampleTest {
    private Ioc ioc;
    private HttpServer httpServer;
    private HttpClient httpClient;

    @BeforeClass
    public void init() {
        ioc = IocBuilder.create().build();
        httpServer = ioc.instance(HttpServer.class).start();
        httpClient = HttpClient.newHttpClient();
    }

    @AfterClass
    public void destroy() {
        httpServer.stop();
    }

    private HttpRequest createGETRequest(String uri) {
        return HttpRequest.newBuilder().uri(URI.create(uri)).build();
    }

    private HttpRequest createPOSTRequest(String uri, String data) {
        if (data != null) {
            return HttpRequest.newBuilder().uri(URI.create(uri)).POST(HttpRequest.BodyPublishers.ofString(data)).build();
        } else {
            return HttpRequest.newBuilder().uri(URI.create(uri)).POST(HttpRequest.BodyPublishers.noBody()).build();
        }
    }

    private String getResponse(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDefaultRouting() throws Exception {
        var request = createGETRequest("http://localhost:8080/default-routing/hello");
        String response = getResponse(request);
        assertEquals(response, "Hello");
    }

    @Test
    public void testAbsoluteRouting() throws Exception {
        var request = createGETRequest("http://localhost:8080/say-hi.html");
        String response = getResponse(request);
        assertEquals(response, "Hi");

        request = createGETRequest("http://localhost:8080/absolute-route/say-hello.html");
        response = getResponse(request);
        assertEquals(response, "Hello");
    }

    @Test
    public void testIndexOtherRouting() throws Exception {
        var request = createGETRequest("http://localhost:8080/");
        String response = getResponse(request);
        assertEquals(response, "Index");

        request = createGETRequest("http://localhost:8080/bla-bla");
        response = getResponse(request);
        assertEquals(response, "Other");
    }

    @Test
    public void testRelativeRouting() throws Exception {
        var request = createGETRequest("http://localhost:8080/relative-routing/say-hola");
        String response = getResponse(request);
        assertEquals(response, "Hola");
    }

    @Test
    public void testPkgRelRouting() throws Exception {
        var request = createGETRequest("http://localhost:8080/api/v1.0/relative/say-hallo");
        String response = getResponse(request);
        assertEquals(response, "Hallo");

        request = createGETRequest("http://localhost:8080/api/v1.0/relative/say-hei");
        response = getResponse(request);
        assertEquals(response, "Hei");
    }

    @Test
    public void testSubmitRouting() throws Exception {
        var request = createPOSTRequest("http://localhost:8080/my-form/submit", null);
        String response = getResponse(request);
        assertEquals(response, "Submit");
    }
}
