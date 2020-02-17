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
package colesico.framework.http.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.ThreadScope;



/**
 * Default HTTP producer
 *
 * @author Vladlen Larionov
 */
@Producer
public class HttpProducer {

    public HttpContext getHttpContext(ThreadScope scope) {
        return scope.get(HttpContext.SCOPE_KEY);
    }

    public HttpRequest getHttpRequest(HttpContext ctx) {
        return ctx.getRequest();
    }

    public HttpResponse getHttpResponse(HttpContext ctx) {
        return ctx.getResponse();
    }
}
