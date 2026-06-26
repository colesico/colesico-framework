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
import colesico.framework.http.HttpResponse;
import colesico.framework.router.assist.UnknownRouteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;

import java.util.Date;

import static colesico.framework.assist.ExceptionUtils.getRootCauseMessage;
import static colesico.framework.assist.ExceptionUtils.toStackTrace;

@Singleton
public class DefaultErrorHandler implements ErrorHandler {

    public static final String CONTENT_TYPE = "text/plain; charset=utf-8";

    protected final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void handleException(Throwable throwable, HttpContext httpContext) {
        String rootMessage = getRootCauseMessage(throwable);

        logger.debug("Unexpected error: " + rootMessage);
        logger.error(toStackTrace(throwable));

        HttpResponse response = httpContext.response();
        if (response.isCommitted()) {
            return;
        }

        StringBuilder out = new StringBuilder();
        out.append("An unexpected error occurred at ")
                .append(new Date().toInstant())
                .append(". See server log for details.");

        try {
            if (throwable instanceof UnknownRouteException) {
                response.setContentType(CONTENT_TYPE);
                response.setStatus(404);
                response.send(out.toString());
            } else {
                response.setContentType(CONTENT_TYPE);
                response.setStatus(500);
                response.send(out.toString());
            }
        } catch (Exception ex) {
            logger.error("Sending error page error: {}", getRootCauseMessage(ex));
        }
    }
}
