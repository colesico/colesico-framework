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

import colesico.framework.http.HttpMethod;
import colesico.framework.router.RequestMethod;
import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.ParamOrigin;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

@Weblet
public class PostParams {

    // http://localhost:8080/post-params/form?action=default-action
    // http://localhost:8080/post-params/form?action=advanced-action?getparam=1
    public HtmlResponse form(String action) {
        String formHtml = """
                <form method='post'>
                    <input type='text' name='formval' value=''/>
                    <input type='submit' value='Submit' formaction='/post-params/%s'/>
                </form>
                """;

        return HtmlResponse.of(String.format(formHtml, action));
    }

    // for http://localhost:8080/post-params/form?action=default-action
    @RequestMethod(HttpMethod.POST)
    public HtmlResponse defaultAction(String formval) {
        return HtmlResponse.of("formval=" + formval);
    }

    //for http://localhost:8080/post-params/form?action=advanced-action?extraparam=1
    @RequestMethod(HttpMethod.POST)
    public HtmlResponse advancedAction(
            /* formval is a get or post param */String formval,
            /* getVal is a get or post param  */@ParamName("extraparam") Integer getVal,
            /* postVal is a post param only */  @ParamName("extraparam") @ParamOrigin(Origin.POST) Integer postVal) {

        return HtmlResponse.of("formval=" + formval + "; extraparam(get)=" + getVal + "; extraparam(post)=" + postVal);
    }
}
