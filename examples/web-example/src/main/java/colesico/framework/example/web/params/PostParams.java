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

package colesico.framework.example.web.params;

import colesico.framework.http.HttpMethod;
import colesico.framework.router.RequestMethod;
import colesico.framework.telehttp.assist.CSRFProtector;
import colesico.framework.telehttp.origin.Origin;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.ParamOrigin;
import colesico.framework.telehttp.response.StringResponse;
import colesico.framework.weblet.Weblet;

import java.text.MessageFormat;

@Weblet
public class PostParams {

    private final CSRFProtector csrfProtector;

    public PostParams(CSRFProtector csrfProtector) {
        this.csrfProtector = csrfProtector;
    }

    // http://localhost:8080/post-params/form?action=default-action
    // http://localhost:8080/post-params/form?action=advanced-action?getparam=1
    public StringResponse form(String action) {
        var response = StringResponse.of();

        var csrfToken = csrfProtector.addToken(response);
        String formHtml = """
                <form method='post'>
                    <input type='text' name='formval' value=''/>
                    <input type='submit' value='Submit' formaction='/post-params/%s'/>
                </form>
                """;

        return response.value(String.format(formHtml, csrfToken, action)).build();
    }

    // for http://localhost:8080/post-params/form?action=default-action
    @RequestMethod(HttpMethod.POST)
    public String defaultAction(String formval) {
        return "formval=" + formval;
    }

    //for http://localhost:8080/post-params/form?action=advanced-action?extraparam=1
    @RequestMethod(HttpMethod.POST)
    public String advancedAction(
            /* formval is a get or post param */String formval,
            /* getVal is a get or post param  */@ParamName("extraparam") Integer getVal,
            /* postVal is a post param only */  @ParamName("extraparam") @ParamOrigin(Origin.POST) Integer postVal) {

        return MessageFormat.format("formval={0}; extraparam(get)={1}; extraparam(post)={2}", formval, getVal, postVal);
    }
}
