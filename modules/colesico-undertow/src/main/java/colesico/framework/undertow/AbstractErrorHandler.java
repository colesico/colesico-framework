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
package colesico.framework.undertow;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.router.UnknownRouteException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * @author Vladlen Larionov
 */
abstract public class AbstractErrorHandler implements ErrorHandler {

    public static final String HTML_CONTENT_TYPE = "text/html; charset=utf-8";

    protected final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    protected final Provider<HttpContext> contextProv;

    public AbstractErrorHandler(Provider<HttpContext> contextProv) {
        this.contextProv = contextProv;
    }

    protected String toStackTrace(Exception cause) {
        if (cause == null) return "";
        StringWriter sw = new StringWriter(1024);
        final PrintWriter pw = new PrintWriter(sw);
        cause.printStackTrace(pw);
        pw.flush();
        return sw.toString();
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
