/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

import colesico.framework.http.HttpContext;
import colesico.framework.httpserver.ErrorHandler;
import colesico.framework.httpserver.HttpServerAttribute;
import colesico.framework.httpserver.RequestProcessor;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.ioc.scope.RequestScope;
import colesico.framework.router.Router;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class UndertowNonBlockingHandler extends RequestProcessor<HttpServerExchange>
        implements HttpHandler {

    private final Supplier<UndertowBlockingHandler> blockingHandlerSup;

    @Inject
    public UndertowNonBlockingHandler(RequestScope requestScope, Router router, ErrorHandler errorHandler, Supplier<UndertowBlockingHandler> blockingHandlerSup) {
        super(requestScope, router, errorHandler);
        this.blockingHandlerSup = blockingHandlerSup;
    }

    @Override
    protected HttpContext createHttpContext(HttpServerExchange rawContext) {
        return new HttpContext(new UndertowHttpRequest(rawContext), new UndertowHttpResponse(rawContext));
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        requestScope.open();
        try (requestScope) {
            var httpContext = bindHttpContext(exchange);
            var invocation = resolve(httpContext);

            if (invocation != null) {
                if (isBlocking(invocation)) {
                    // Run processing to separate thread
                    exchange.dispatch(blockingHandlerSup.get(invocation));
                    return;
                }
                execute(invocation, httpContext);
            }

        } catch (Throwable fatal) {
            log.error("Fatal error during request processing: {}", fatal.getMessage());
        } finally {
            if (!exchange.isDispatched()) {
                exchange.endExchange();
            }
        }
    }

    protected boolean isBlocking(Router.Invocation invocation) {
        Map<String, String> attributes = invocation.action().attributes();
        return attributes == null
                || (!"true".equals(attributes.get(HttpServerAttribute.NON_BLOCKING)));
    }
}
