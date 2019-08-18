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

package colesico.framework.restlet.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpException;
import colesico.framework.http.HttpRequest;
import colesico.framework.ioc.ThreadScope;
import colesico.framework.restlet.RestletErrorResponse;
import colesico.framework.restlet.teleapi.RestletDataPort;
import colesico.framework.restlet.teleapi.RestletTeleDriver;
import colesico.framework.security.AuthorityRequiredException;
import colesico.framework.security.PrincipalRequiredException;
import colesico.framework.service.ApplicationException;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;
import colesico.framework.validation.ValidationException;
import colesico.framework.weblet.teleapi.InvokeContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class RestletTeleDriverImpl implements RestletTeleDriver {

    public static final String X_REQUESTED_WITH_HEADER = "X-Requested-With";
    public static final String X_REQUESTED_WITH_HEADER_VAL = "XMLHttpRequest";

    protected final Logger log = LoggerFactory.getLogger(RestletTeleDriver.class);

    protected final ThreadScope threadScope;
    protected final Provider<HttpContext> httpContextProv;
    protected final RestletDataPort restletDataPort;

    @Inject
    public RestletTeleDriverImpl(ThreadScope threadScope, Provider<HttpContext> httpContextProv, RestletDataPort restletDataPort) {
        this.threadScope = threadScope;
        this.httpContextProv = httpContextProv;
        this.restletDataPort = restletDataPort;
    }

    @Override
    public <F> void invoke(F framlet, Binder<F, RestletDataPort> binder, InvokeContext invCtx) {
        // Set data port to be accessible
        threadScope.put(DataPort.SCOPE_KEY, restletDataPort);
        // Retrieve http context
        HttpContext httpCtx = httpContextProv.get();
        try {
            invokeImpl(framlet, binder, invCtx, httpCtx);
        } catch (Exception ex) {
            handleCommonError(ex, 500, httpCtx);
        }
    }

    protected <F> void invokeImpl(F framlet, Binder binder, InvokeContext invCtx, HttpContext context) {
        HttpRequest httpRequest = context.getRequest();
        try {

            // CSRF protection
            String xRequestedWith = httpRequest.getHeaders().get(X_REQUESTED_WITH_HEADER);
            if (!X_REQUESTED_WITH_HEADER_VAL.equals(xRequestedWith)) {
                throw new ApplicationException("Http header '" + X_REQUESTED_WITH_HEADER + "=" + X_REQUESTED_WITH_HEADER_VAL + "' required");
            }

            // Invoke tele-method
            binder.invoke(framlet, restletDataPort);

        } catch (HttpException hex) {
            if (hex.getCause() != null) {
                if (hex.getCause() instanceof ValidationException) {
                    handleValidationError((ValidationException) hex.getCause(), hex.getHttpCode(), context);
                } else {
                    handleCommonError(hex.getCause(), hex.getHttpCode(), context);
                }
            } else {
                handleCommonError(hex, hex.getHttpCode(), context);
            }
        } catch (ValidationException vex) {
            handleValidationError(vex, 400, context);
        } catch (PrincipalRequiredException prex) {
            handleCommonError(prex, 401, context);
        } catch (AuthorityRequiredException arex) {
            handleCommonError(arex, 403, context);
        }
    }

    protected String getMessage(Throwable ex) {
        Throwable e = ex;
        List<String> messages = new ArrayList<>();

        int depth = 0;
        while (e != null) {
            String message = e.getMessage();
            if (message == null) {
                message = e.getClass().getName();
            }
            messages.add(message);
            if (e.getCause() == e) {
                e = null;
            } else {
                if (depth++ < 8) {
                    e = e.getCause();
                } else {
                    e = null;
                }
            }
        }
        return StringUtils.join(messages, "; ");
    }

    protected void handleCommonError(Throwable ex, int httpCode, HttpContext context) {
        String errMsg = MessageFormat.format("Restlet error: {0}", ExceptionUtils.getRootCauseMessage(ex));
        if (ex instanceof ApplicationException) {
            log.warn(errMsg, ex);
        } else {
            log.error(errMsg, ex);
        }
        RestletErrorResponse response = new RestletErrorResponse(context.getRequest().getRequestURI(), httpCode, getMessage(ex));
        restletDataPort.sendError(response, httpCode);
    }

    protected void handleValidationError(ValidationException ex, int httpCode, HttpContext context) {
        String errMsg = MessageFormat.format("Restlet validation error: {0}", ExceptionUtils.getRootCauseMessage(ex));
        log.warn(errMsg);
        RestletErrorResponse response = new RestletErrorResponse(context.getRequest().getRequestURI(), httpCode, ex.getIssue());
        restletDataPort.sendError(response, httpCode);
    }

}
