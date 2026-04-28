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

package colesico.framework.httpserver;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.ioc.scope.RequestScope;
import colesico.framework.router.Router;
import colesico.framework.router.assist.UnknownRouteException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Basic abstract http request processor.
 * This class to be used for http server implementations.
 *
 * @param <C> Raw context to build http response and request from it
 */
abstract public class RequestProcessor<C> {

    protected final Logger log = LoggerFactory.getLogger(RequestProcessor.class);
    protected final RequestScope requestScope;
    protected final Router router;
    protected final ErrorHandler errorHandler;

    public RequestProcessor(RequestScope requestScope, Router router, ErrorHandler errorHandler) {
        this.requestScope = requestScope;
        this.router = router;
        this.errorHandler = errorHandler;
    }

    abstract protected HttpContext createHttpContext(C rawContext);

    protected void handleRequest(C rawContext) {
        requestScope.open();
        try (requestScope) {
            var httpContext = bindHttpContext(rawContext);
            var invocation = resolve(httpContext);
            if (invocation != null) {
                execute(invocation, httpContext);
            }
        } catch (Throwable fatal) {
            log.error("Fatal error during request processing: {}", fatal.getMessage());
        }
    }

    protected Router.Invocation resolve(HttpContext httpContext) {
        HttpMethod httpMethod = httpContext.request().requestMethod();
        String requestUri = httpContext.request().requestURI();
        Optional<Router.Invocation> resolution;
        try {
            resolution = router.resolve(httpMethod, requestUri);
        } catch (Throwable t) {
            handleException(t, httpContext);
            return null;
        }

        if (resolution.isEmpty()) {
            handleException(new UnknownRouteException(httpMethod, requestUri), httpContext);
            return null;
        }

        return resolution.get();
    }

    /**
     * Override this method to custom execute invocation
     */
    protected void execute(Router.Invocation invocation, HttpContext httpContext) {
        try {
            router.execute(invocation);
        } catch (Throwable t) {
            handleException(t, httpContext);
        }
    }

    protected HttpContext bindHttpContext(C rawContext) {
        var httpContext = createHttpContext(rawContext);
        requestScope.put(HttpContext.SCOPE_KEY, httpContext);
        return httpContext;
    }

    protected void handleException(Throwable t, HttpContext httpContext) {
        log.error("Request processing error: {}", ExceptionUtils.getRootCauseMessage(t));
        try {
            errorHandler.handleException(t, httpContext);
        } catch (Throwable t2) {
            log.error("Error handler failed: {}", ExceptionUtils.getRootCauseMessage(t2));
        }
    }
}