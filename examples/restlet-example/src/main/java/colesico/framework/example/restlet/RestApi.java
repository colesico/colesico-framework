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

package colesico.framework.example.restlet;

import colesico.framework.example.restlet.customexception.CustomException;
import colesico.framework.http.HttpMethod;
import colesico.framework.ioc.listener.PostConstruct;
import colesico.framework.restlet.Restlet;
import colesico.framework.restlet.teleapi.RestletResponseWriter;
import colesico.framework.router.RequestMethod;
import colesico.framework.router.Route;
import colesico.framework.router.RouteAttribute;
import colesico.framework.restlet.teleapi.writer.PlainTextWriter;

import java.util.Arrays;
import java.util.List;

import static colesico.framework.httpserver.HttpServerAttribute.NON_BLOCKING;

/**
 * Mandatory http header X-Requested-With:XMLHttpRequest
 */
@Restlet
public class RestApi {

    private List<User> users;

    @PostConstruct
    public void init() {
        users = Arrays.asList(new User(1L, "Ivan"), new User(2L, "John"));
    }

    /**
     * GET http://localhost:8080/rest-api/list
     */
    public List<User> list() {
        return users;
    }

    /**
     * GET http://localhost:8080/rest-api/find?id=1
     */
    @Route("find")
    public User get(long id) {
        return new User(id, "Katherine");
    }

    /**
     * POST  http://localhost:8080/rest-api/save +  data {"id":1,"name":"Anne"}
     */
    @RequestMethod(HttpMethod.POST)
    public long save(User user) {
        return user.getId();
    }

    /**
     * Non blocking processing example  (see undertow non-blocking XNIO channels)
     * POST  http://localhost:8080/rest-api/non-blocking
     */
    @RouteAttribute(name = NON_BLOCKING, value = "true")
    @RestletResponseWriter(PlainTextWriter.class)
    public String nonBlocking() {
        return "NonBlocking";
    }


    /**
     * Custom exception example
     */
    public Boolean customException() {
        throw new CustomException("Custom exception", List.of("Payload1", "Payload2"));
    }

}
