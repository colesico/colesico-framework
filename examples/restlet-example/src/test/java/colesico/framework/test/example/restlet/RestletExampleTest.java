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

package colesico.framework.test.example.restlet;

import colesico.framework.example.restlet.User;
import colesico.framework.httpserver.HttpServer;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.conditional.TestCondition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;

public class RestletExampleTest {
    private Ioc ioc;
    private HttpServer httpServer;
    private HttpClient httpClient;
    private Gson gson = new Gson();
    private Logger logger = LoggerFactory.getLogger(RestletExampleTest.class);

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

    private String requestGET(String url) throws Exception {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                // .header("X-Requested-With", "XMLHttpRequest")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private String requestPOST(String url, String jsonRequest) throws Exception {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                //.header("X-Requested-With", "XMLHttpRequest")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    @Test
    public void testList() throws Exception {
        List<User> users = gson.fromJson(requestGET("http://localhost:8085/rest-api/list"), new TypeToken<List<User>>() {
        }.getType());
        assertEquals("Ivan", users.get(0).getName());
        assertEquals("John", users.get(1).getName());
    }

    @Test
    public void testFind() throws Exception {
        User user = gson.fromJson(requestGET("http://localhost:8085/rest-api/find?id=1"), (Type) User.class);
        assertEquals("Katherine", user.getName());
        assertEquals(user.getId().longValue(), 1L);
    }

    @Test
    public void testSave() throws Exception {
        User user = new User();
        user.setId(2L);
        user.setName("AName");
        Long id = gson.fromJson(requestPOST("http://localhost:8085/rest-api/save", gson.toJson(user)), Long.class);
        assertEquals(id.longValue(), 2L);
    }

    @Test
    public void testNonBlocking() throws Exception {
        String result = requestGET("http://localhost:8085/rest-api/non-blocking");
        //assertEquals("NonBlocking",result);
    }

    @Test
    public void testJsonFields() throws Exception {
        String resultStr = requestPOST("http://localhost:8085/rest-api/json-fields?val=test", "{id:1,name:Vladlen}");
        Map resultMap = gson.fromJson(resultStr, Map.class);
        assertEquals("Vladlen", resultMap.get("name"));
        assertEquals("test", resultMap.get("val"));
    }
}
