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

package colesico.framework.weblet.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.router.RouterContext;
import colesico.framework.weblet.teleapi.WebletTeleReader;

import javax.inject.Provider;

/**
 * @author Vladlen Larionov
 */
abstract public class AbstractReader<V> implements WebletTeleReader<V> {

    protected final Provider<RouterContext> routerContextProv;
    protected final Provider<HttpContext> httpContextProv;

    protected final HttpRequest getHttpRequest() {
        return httpContextProv.get().getRequest();
    }

    protected final HttpResponse getHttpResponse() {
        return httpContextProv.get().getResponse();
    }

    protected final RouterContext getRouterContext() {
        return routerContextProv.get();
    }

    public AbstractReader(Provider<RouterContext> routerContextProv, Provider<HttpContext> httpContextProv) {
        this.routerContextProv = routerContextProv;
        this.httpContextProv = httpContextProv;
    }
}
