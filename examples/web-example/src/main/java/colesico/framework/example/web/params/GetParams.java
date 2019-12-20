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

package colesico.framework.example.web.params;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

import java.text.MessageFormat;

@Weblet
public class GetParams {

    // http://localhost:8080/get-params/a-b?a=test&b=100

    @Route("a-b")
    public HtmlResponse printParams(String a, Integer b ){
        return HtmlResponse.of(MessageFormat.format("a={0}, b={1}",a,b));
    }

    // http://localhost:8080/get-params/path/test/100
    @Route("path/:a/:b")
    public HtmlResponse printRouteParams(String a, Integer b ){
        return HtmlResponse.of(MessageFormat.format("a={0}, b={1}",a,b));
    }

    // http://localhost:8080/get-params/path-s/foo/blabla
    @Route("path-s/*")
    public HtmlResponse printRouteSuffix(String routeSuffix ){
        return HtmlResponse.of(MessageFormat.format("routeSuffix={0}",routeSuffix));
    }
}
