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

package colesico.framework.weblet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.router.Router;
import colesico.framework.weblet.RedirectResponse;
import colesico.framework.weblet.teleapi.WebletTWContext;
import colesico.framework.weblet.teleapi.WebletTeleWriter;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Vladlen Larionov
 */
public final class ForwardWriter extends WebletTeleWriter<RedirectResponse> {

    protected final Router router;

    @Inject
    public ForwardWriter(Provider<HttpContext> httpContextProv, Router router) {
        super(httpContextProv);
        this.router = router;
    }

    @Override
    public void write(RedirectResponse navResp, WebletTWContext wrContext) {
        navResp.forward(router);
    }
}
