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

package colesico.framework.example.routing;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Weblet relative uri example.
 * This weblet uses default route './relative-routing'
 */
@Weblet
public class RelativeRouting {

    // say-hola route is not starting with '/' so it is relative route
    // Final URL: http://localhost:8080/relative-routing/say-hola
    // './relative-routing' part is derived  from default weblet route.
    @Route("say-hola")
    public HtmlResponse hola() {
        return HtmlResponse.of("Hola");
    }
}
