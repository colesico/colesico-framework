/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */
package colesico.framework.undertow.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.ThreadScope;
import colesico.framework.router.Router;
import colesico.framework.undertow.ErrorHandler;
import colesico.framework.undertow.RouterHandler;
import colesico.framework.undertow.UndertowConfigPrototype;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class RouterHandlerImpl implements RouterHandler {

    protected final Logger log = LoggerFactory.getLogger(RouterHandler.class);

    protected final ThreadScope threadScope;
    protected final Router router;
    protected final ErrorHandler errorHandler;
    protected final UndertowConfigPrototype config;

    @Inject
    public RouterHandlerImpl(ThreadScope threadScope,
                             Router router,
                             ErrorHandler errorHandler,
                             UndertowConfigPrototype config) {
        this.threadScope = threadScope;
        this.router = router;
        this.errorHandler = errorHandler;
        this.config = config;
    }

    protected HttpRequest createHttpRequest(HttpServerExchange exchange) {
        return new HttpRequestImpl(exchange);
    }

    protected HttpResponse createHttpResponse(HttpServerExchange exchange) {
        return new HttpResponseImpl(exchange);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        //TODO: use configuration run in blocking thread with url mapping?
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        long startTime = System.nanoTime();

        // Create contexts
        HttpRequest httpRequest = createHttpRequest(exchange);
        HttpResponse httpResponse = createHttpResponse(exchange);
        HttpContext httpContext = new HttpContext(httpRequest, httpResponse);

        // Put contexts to process scope
        threadScope.init();
        threadScope.put(HttpContext.SCOPE_KEY, httpContext);

        try {
            router.perform(httpRequest.getRequestMethod(), httpRequest.getRequestURI());
        } catch (Exception ex) {
            String errMsg = MessageFormat.format("Request processing error: {0}", ExceptionUtils.getRootCauseMessage(ex));
            log.error(errMsg);
            try {
                errorHandler.handleException(ex);
            } catch (Exception ex2) {
                String errMsg2 = MessageFormat.format("Handle error error: {0}", ExceptionUtils.getRootCauseMessage(ex2));
                log.error(errMsg2);
            }
        } finally {
            threadScope.destroy();
            if (log.isDebugEnabled()) {
                long stopTime = System.nanoTime();
                long elapsedTime = stopTime - startTime;
                log.debug("Request '" + httpRequest.getRequestURI() + "' processing time: " + TimeUnit.NANOSECONDS.toMicros(elapsedTime) + " micro sec");
            }
        }
    }

}
