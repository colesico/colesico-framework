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

package colesico.framework.example.routes;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Absolute route starts with '/'
 */
@Weblet
@Route("/absolute-route")
public class AbsoluteRoute {

    // This is absolute route, starts with '/'
    // http://localhost:8080/say-hi.html
    @Route("/say-hi.html")
    public HtmlResponse hi() {
        return HtmlResponse.of("Hi!");
    }

    // This is relative route regarding weblet route.
    // Relative route is not starts with '/', or may starts from './'
    // http://localhost:8080/absolute-route/say-hello.html
    @Route("say-hello.html")
    public HtmlResponse hello() {
        return HtmlResponse.of("Hello!");
    }

}
