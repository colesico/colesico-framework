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

package colesico.framework.weblet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.router.Router;
import colesico.framework.weblet.response.ForwardResponse;
import colesico.framework.weblet.teleapi.WebletWriteOptions;
import colesico.framework.weblet.teleapi.WebletTeleWriter;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

/**
 * Performs forward operation
 */
public final class ForwardWriter implements WebletTeleWriter<ForwardResponse> {

    private final Router router;
    private final Provider<HttpContext> httpContext;

    @Inject
    public ForwardWriter(Router router, Provider<HttpContext> httpContext) {
        this.router = router;
        this.httpContext = httpContext;
    }

    @Override
    public void write(ForwardResponse value, Class<ForwardResponse> baseType, WebletWriteOptions options) {
        value.navigation().forward(router, httpContext.get());
    }

}
