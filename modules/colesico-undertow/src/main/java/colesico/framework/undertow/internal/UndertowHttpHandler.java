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
import colesico.framework.httpserver.HttpServerAttribute;
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
public class UndertowHttpHandler extends RequestProcessor<HttpServerExchange> implements HttpHandler {

    private final Supplier<UndertowBlockingHandler> blockingHandlerSup;

    @Inject
    public UndertowHttpHandler(ThreadScope threadScope,
                               Router router,
                               ErrorHandler errorHandler,
                               Supplier<UndertowBlockingHandler> blockingHandlerSup) {
        super(threadScope, router, errorHandler);
        this.blockingHandlerSup = blockingHandlerSup;
    }

    @Override
    protected HttpRequest createHttpRequest(HttpServerExchange exchange) {
        return new UndertowHttpRequest(exchange);
    }

    @Override
    protected HttpResponse createHttpResponse(HttpServerExchange exchange) {
        return new UndertowHttpResponse(exchange);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        // Retrieve action resolution
        HttpMethod requestMethod = HttpMethod.of(exchange.getRequestMethod().toString());
        String requestUri = StringUtils.substringBefore(exchange.getRequestURI(), "?");
        // 
        ActionResolution actionResolution = resolveAction(requestMethod, requestUri, exchange);

        if (actionResolution != null) {
            // Check  blocking|non-blocking processing
            Map<String, String> routeAttributes = actionResolution.getRouteAction().getAttributes();

            boolean blockingProcessing = routeAttributes == null
                    || (!"true".equals(routeAttributes.get(HttpServerAttribute.NON_BLOCKING)));

            if (blockingProcessing) {
                // Dispatching to a worker thread
                exchange.startBlocking();
                if (exchange.isInIoThread()) {
                    // Create blocking handler
                    HttpHandler blockingHandler = blockingHandlerSup.get(actionResolution);
                    exchange.dispatch(blockingHandler);
                    return;
                }
            }
            performAction(actionResolution, exchange);
        }
        exchange.endExchange();
    }
}
