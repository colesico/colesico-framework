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
import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.ThreadScope;
import colesico.framework.router.Router;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

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

    public void processRequest(C context) {
        // Create contexts
        HttpRequest httpRequest = createHttpRequest(context);
        HttpResponse httpResponse = createHttpResponse(context);
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
                errorHandler.handleException(ex, httpContext);
            } catch (Exception ex2) {
                String errMsg2 = MessageFormat.format("Handling exception error: {0}", ExceptionUtils.getRootCauseMessage(ex2));
                log.error(errMsg2);
            }
        } finally {
            threadScope.destroy();
        }
    }
}
