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

package colesico.framework.example.web.routes;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Weblet relative path example
 * see package-info.java  @Route annotation
 */
@Weblet
@Route("./relative")
public class Relative2 {

    // http://localhost:8080/my-service/v1.0/relative/say-hi
    // /my-service/v1.0 + /relative + /say-hi
    @Route("say-hi")
    public HtmlResponse hi() {
        return HtmlResponse.of("Hi from 1");
    }

    // http://localhost:8080/my-service/v1.0/relative/say-hi-2
    @Route("./say-hi-2")
    public HtmlResponse hi2() {
        return HtmlResponse.of("Hi from 2");
    }

    // http://localhost:8080/my-service/v1.0/relative
    @Route("./")
    public HtmlResponse hi3() {
        return HtmlResponse.of("Hi from weblet index!");
    }
}
