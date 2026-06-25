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

package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.router.Router;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.response.RedirectResponse;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class RedirectWriter implements TeleHttpWriter<RedirectResponse, TeleHttpWriteOptions> {

    private final Router router;
    private final Provider<HttpContext> httpContext;

    public RedirectWriter(Router router, Provider<HttpContext> httpContext) {
        this.router = router;
        this.httpContext = httpContext;
    }

    @Override
    public void write(RedirectResponse value, TeleHttpWriteOptions options) {
        value.navigation().redirect(router, httpContext.get());
    }

}
