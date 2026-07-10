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

package colesico.framework.example.routing;

import colesico.framework.httprouter.Route;
import colesico.framework.weblet.Weblet;

/**
 * Absolute route starts with '/'
 */
@Weblet
@Route("/absolute-route")
public class AbsoluteRouting {

    // This is the method absolute route, starts with '/'
    // Final URL: http://localhost:8080/say-hi.html
    @Route("/say-hi.html")
    public Responses hi() {
        return Responses.object("Hi");
    }

    // This is relative route regarding weblet route (/absolute-route)
    // Relative route is not starts with '/', or may starts from './'
    // Final URL: http://localhost:8080/absolute-route/say-hello.html
    @Route("say-hello.html") // or  @Route("./say-hello.html")
    public Responses hello() {
        return Responses.object("Hello");
    }

}
