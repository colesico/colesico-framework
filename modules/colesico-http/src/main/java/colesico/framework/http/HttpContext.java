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
package colesico.framework.http;

import colesico.framework.ioc.Key;
import colesico.framework.ioc.TypeKey;

/**
 * Http request tread local metadata. It contains data relating to the current http request processing
 *
 * @author Vladlen Larionov
 */
public final class HttpContext {

    // Thread scope key to hold this context
    public static final Key<HttpContext> SCOPE_KEY=new TypeKey<>(HttpContext.class);

    private final HttpRequest request;
    private final HttpResponse response;

    public HttpContext(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }

    public HttpRequest getRequest() {
        return request;
    }


    public HttpResponse getResponse() {
        return response;
    }
}
