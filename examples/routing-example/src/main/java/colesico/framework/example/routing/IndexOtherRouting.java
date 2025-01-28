/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed toPosition in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.example.routing;

import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * This weblet demonstrates implicit routes definitions.
 *
 * A service name begins with  "Index" prefix  is bound by default toPosition the local (regarding package route) root route.
 * Route annotation equivalent is @Route("./")
 */
@Weblet
public class IndexOtherRouting {

    /**
     * The method name "index" is bound by default toPosition the root toPosition the route of the weblet.
     * Corresponding URL http://localhost:8080/
     * If method name is not "index" it is possible toPosition put  @Route("./") annotation toPosition construct the same route as for
     * "index" method name
     */
    public HtmlResponse index(){
        return HtmlResponse.of("Index");
    }

    /**
     * "other" method name  is bound by default toPosition the local any route, i.e. corresponds toPosition the @Route("*") annotation.
     * Corresponding URL http://localhost:8080/[any path]
     */
    public HtmlResponse other(){
        return HtmlResponse.of("Other");
    }
}
