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

package colesico.framework.undertow.internal;

import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.httpserver.ErrorHandler;
import colesico.framework.httpserver.HttpServerAttributes;
import colesico.framework.httpserver.RequestProcessor;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.ActionResolution;
import colesico.framework.router.Router;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class UndertowRequestProcessor extends RequestProcessor<HttpServerExchange> implements HttpHandler {

    private final Supplier<UndertowBlockingRequestProcessor> blockingProcessourSup;

    @Inject
    public UndertowRequestProcessor(ThreadScope threadScope,
                                    Router router,
                                    ErrorHandler errorHandler,
                                    Supplier<UndertowBlockingRequestProcessor> blockingProcessourSup) {
        super(threadScope, router, errorHandler);
        this.blockingProcessourSup = blockingProcessourSup;
    }

    @Override
    protected HttpRequest createHttpRequest(HttpServerExchange exchange) {
        return new HttpRequestImpl(exchange);
    }

    @Override
    protected HttpResponse createHttpResponse(HttpServerExchange exchange) {
        return new HttpResponseImpl(exchange);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        // Retrieve action resolution
        HttpMethod requestMethod = HttpMethod.of(exchange.getRequestMethod().toString());
        String requestUri = StringUtils.substringBefore(exchange.getRequestURI(), "?");
        ActionResolution resolution = resolveAction(requestMethod, requestUri, exchange);

        if (resolution != null) {
            // Check  blocking|nonblocking processing
            Map<String, String> routeAttributes = resolution.getRouteAction().getAttributes();
            boolean blockingProcessing = routeAttributes == null || (!routeAttributes.containsKey(HttpServerAttributes.NON_BLOCKING_PROCESSING));
            if (blockingProcessing) {
                // Dispatching to a worker thread
                exchange.startBlocking();
                if (exchange.isInIoThread()) {
                    exchange.dispatch(blockingProcessourSup.get(resolution));
                    return;
                }
            }
            performAction(resolution, exchange);
        }
        exchange.endExchange();
    }
}
