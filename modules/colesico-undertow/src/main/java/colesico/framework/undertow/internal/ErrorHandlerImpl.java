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

package colesico.framework.undertow.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.router.UnknownRouteException;
import colesico.framework.undertow.AbstractErrorHandler;
import colesico.framework.undertow.ErrorHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Date;

@Singleton
public class ErrorHandlerImpl extends AbstractErrorHandler {

    public ErrorHandlerImpl(Provider<HttpContext> contextProv) {
        super(contextProv);
    }

    @Override
    public void handleException(Exception exception) {
        String rootMessage = ExceptionUtils.getRootCauseMessage(exception);

        logger.error("Unexpected error: " + rootMessage);
        logger.error(toStackTrace(exception));

        StringBuilder out = new StringBuilder("<!doctype html>");
        out.append("<html>");
        out.append("<head>");
        out.append("<title>").append("Error").append("</title>");
        out.append("</head>");
        out.append("<body>");
        out.append("An unexpected error occurred at ").append(new Date().toInstant()).append(". See server log for details.");
        out.append("</body>");
        out.append("</html>");

        HttpResponse response = contextProv.get().getResponse();
        try {
            if (exception instanceof UnknownRouteException) {
                response.sendText(out.toString(), HTML_CONTENT_TYPE, 404);
            } else {
                response.sendText(out.toString(), HTML_CONTENT_TYPE, 500);
            }
        } catch (Exception ex) {
            logger.error("Sending error page error: " + ExceptionUtils.getRootCauseMessage(ex));
        }
    }
}
