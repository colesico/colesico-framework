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

import colesico.framework.http.HttpMethod;
import colesico.framework.router.RequestMethod;
import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * POST request handling example.
 */
@Weblet
@Route("/my-form")
public class SubmitForm {

    /**
     * Customize HTTP request method form GET (by default) to POST
     * Corresponding URL POST http://localhost:8080/my-form/submit
     * @return
     */
    @RequestMethod(HttpMethod.POST)
    public HtmlResponse submit() {
        return HtmlResponse.of("Submitted");
    }
}
