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

package colesico.framework.example.routing;

import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Weblet default route example.
 * <p>
 * Here is no @Route annotation on weblet and it is assumed that the route to be the weblet name in
 * camel case transformed to snake case:
 * DefaultRouting -> ./default-routing
 */
@Weblet
public class DefaultRouting {

    /**
     * The route for the method without @Route annotation is derived from method name transformed to snake case notation.
     * Corresponding URL GET http://localhost:8080/default-routing/hello    ./default-routing - part from weblet default route
     */
    public HtmlResponse hello() {
        return HtmlResponse.of("Hello");
    }

}
