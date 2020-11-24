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

package colesico.framework.httpserver;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.ActionResolution;
import colesico.framework.router.Router;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic abstract http request processor.
 * This class to be used for http server implementations.
 */
abstract public class RequestProcessor<C> {

    protected final Logger log = LoggerFactory.getLogger(HttpServer.class);

    protected final ThreadScope threadScope;
    protected final Router router;
    protected final ErrorHandler errorHandler;


    public RequestProcessor(ThreadScope threadScope, Router router, ErrorHandler errorHandler) {
        this.threadScope = threadScope;
        this.router = router;
        this.errorHandler = errorHandler;
    }

    abstract protected HttpRequest createHttpRequest(C context);

    abstract protected HttpResponse createHttpResponse(C context);

    protected ActionResolution resolveAction(HttpMethod requestHttpMethod, String requestUri, C context) {
        try {
            return router.resolveAction(requestHttpMethod, requestUri);
        } catch (Exception e) {
            // Init http context and perform error handling
            threadScope.init();
            try {
                final HttpContext httpContext = initHttpContext(context);
                handleException(e, httpContext);
            } finally {
                threadScope.destroy();
            }
            return null;
        }
    }

    protected void performAction(ActionResolution resolution, C context) {
        // Init http context
        threadScope.init();
        final HttpContext httpContext = initHttpContext(context);
        try {
            // Perform router action
            router.performAction(resolution);
        } catch (Exception e) {
            handleException(e, httpContext);
        } finally {
            threadScope.destroy();
        }
    }

    protected void handleException(Exception ex, HttpContext httpContext) {
        log.error("Request processing error: {}", ExceptionUtils.getRootCauseMessage(ex));
        try {
            errorHandler.handleException(ex, httpContext);
        } catch (Exception ex2) {
            log.error("Handling exception error: {}", ExceptionUtils.getRootCauseMessage(ex2));
        }
    }

    /**
     * Creates http context instance and puts it to thread scope
     */
    protected HttpContext initHttpContext(C context) {
        // Create contexts
        HttpRequest httpRequest = createHttpRequest(context);
        HttpResponse httpResponse = createHttpResponse(context);
        HttpContext httpContext = new HttpContext(httpRequest, httpResponse);
        // Put context to thread scope
        threadScope.put(HttpContext.SCOPE_KEY, httpContext);
        return httpContext;
    }


}
