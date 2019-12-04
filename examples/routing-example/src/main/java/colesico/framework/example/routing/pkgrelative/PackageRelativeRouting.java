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

package colesico.framework.example.routing.pkgrelative;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Weblet relative route example
 * see package-info.java  @Route annotation
 */
@Weblet
@Route("./relative") // or just @Route("relative")
public class PackageRelativeRouting {

    // http://localhost:8080/api/v1.0/relative/say-hallo
    // /api/v1.0 + /relative + /say-hallo
    @Route("say-hallo")
    public HtmlResponse hallo() {
        return HtmlResponse.of("Hallo");
    }

    // http://localhost:8080/api/v1.0/relative/say-hei
    @Route("./say-hei")
    // Equivalent @Route("say-hei")
    public HtmlResponse hei() {
        return HtmlResponse.of("Hei");
    }

}
