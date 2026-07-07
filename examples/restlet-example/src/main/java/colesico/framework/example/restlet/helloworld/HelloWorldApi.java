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

package colesico.framework.example.restlet.helloworld;

import colesico.framework.http.HttpMethod;
import colesico.framework.ioc.listener.PostConstruct;
import colesico.framework.restlet.Restlet;
import colesico.framework.router.RequestMethod;
import colesico.framework.router.Route;
import colesico.framework.service.LocalMethod;

import java.util.Arrays;
import java.util.List;

@Restlet
@Route("hello-world")
public class HelloWorldApi {

    private List<User> users;

    @PostConstruct
    @LocalMethod
    public void init() {
        users = Arrays.asList(new User(1L, "Ivan"), new User(2L, "John"));
    }

    /**
     * GET http://localhost:8080/hello-world/list
     */
    public List<User> list() {
        return users;
    }

    /**
     * GET http://localhost:8080/hello-world/find?id=1
     */
    @Route("find")
    public User get(long id) {
        return new User(id, "Katherine");
    }

    /**
     * POST http://localhost:8080/hello-world/save +  data {"id":1,"name":"Anne"}
     */
    @RequestMethod(HttpMethod.POST)
    public long save(User user) {
        return user.getId();
    }


}
