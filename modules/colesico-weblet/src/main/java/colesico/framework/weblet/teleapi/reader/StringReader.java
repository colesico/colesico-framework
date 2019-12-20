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

package colesico.framework.weblet.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.router.RouterContext;
import colesico.framework.weblet.teleapi.ReaderContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class StringReader extends AbstractReader<String> {

    @Inject
    public StringReader(javax.inject.Provider<RouterContext> routerContextProv, javax.inject.Provider<HttpContext> httpContextProv) {
        super(routerContextProv, httpContextProv);
    }

    @Override
    public String read(ReaderContext ctx) {
        return ctx.getString(getRouterContext(), getHttpRequest());
    }
}
