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

package colesico.framework.restlet.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.restlet.RestletConfigPrototype;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.restlet.teleapi.jsonrequest.JsonRequest;
import colesico.framework.service.ApplicationException;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.MethodInvoker;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class RestletTeleDriverImpl implements RestletTeleDriver {

    public static final String X_REQUESTED_WITH_HEADER = "X-Requested-With";
    public static final String X_REQUESTED_WITH_HEADER_VAL = "XMLHttpRequest";

    protected final Logger logger = LoggerFactory.getLogger(RestletTeleDriver.class);

    protected final RestletConfigPrototype config;

    protected final ThreadScope threadScope;
    protected final Provider<HttpContext> httpContextProv;
    protected final RestletDataPort dataPort;
    protected final JsonRequestFactory jsonRequestFactory;
    protected final Polysupplier<RestletRequestListener> reqListenerSup;
    protected final Polysupplier<RestletResponseListener> respListenerSup;

    @Inject
    public RestletTeleDriverImpl(RestletConfigPrototype config,
                                 ThreadScope threadScope,
                                 Provider<HttpContext> httpContextProv,
                                 RestletDataPort dataPort,
                                 JsonRequestFactory jsonRequestFactory,
                                 Polysupplier<RestletRequestListener> reqListenerSup,
                                 Polysupplier<RestletResponseListener> respListenerSup) {
        this.config = config;
        this.threadScope = threadScope;
        this.httpContextProv = httpContextProv;
        this.dataPort = dataPort;
        this.jsonRequestFactory = jsonRequestFactory;
        this.reqListenerSup = reqListenerSup;
        this.respListenerSup = respListenerSup;
    }

    @Override
    public <S> void invoke(S service, MethodInvoker<S, RestletDataPort> invoker, RestletTIContext invContext) {
        // Set data port to be accessible
        threadScope.put(DataPort.SCOPE_KEY, dataPort);
        // Retrieve http context
        HttpContext httpContext = httpContextProv.get();
        // Retrieve http request
        HttpRequest httpRequest = httpContext.getRequest();
        try {
            // Request listener notification
            notifyRequestListener(httpContext, service);

            // CSRF protection
            if (config.enableCSFRProtection()) {
                guardCSFR(httpRequest);
            }

            // Init json request if defined
            if (invContext != null && invContext.getJsonRequestType() != null) {
                JsonRequest jr = jsonRequestFactory.getJsonRequest(invContext.getJsonRequestType());
                threadScope.put(JsonRequest.SCOPE_KEY, jr);
            }

            // Invoke tele-method
            invoker.invoke(service, dataPort);

        } catch (Exception ex) {
            if (ex instanceof ApplicationException) {
                logger.warn("Application exception: " + ExceptionUtils.getRootCauseMessage(ex));
            } else {
                logger.error("Unexpected error:" + ExceptionUtils.getRootCauseMessage(ex));
            }
            try {
                dataPort.writeError(ex);
            } catch (Exception e) {
                logger.error("Writing exception error: {}", ExceptionUtils.getRootCauseMessage(e));
            }
        } finally {
            notifyResponseListener(httpContext);
        }
    }


    protected void guardCSFR(HttpRequest httpRequest) {
        String xRequestedWith = httpRequest.getHeaders().get(X_REQUESTED_WITH_HEADER);
        if (!X_REQUESTED_WITH_HEADER_VAL.equals(xRequestedWith)) {
            throw new ApplicationException("Http header '" + X_REQUESTED_WITH_HEADER + "=" + X_REQUESTED_WITH_HEADER_VAL + "' required");
        }
    }

    protected void notifyRequestListener(final HttpContext context, final Object service) {
        if (reqListenerSup.isNotEmpty()) {
            reqListenerSup.forEach(s -> s.onRequest(context, dataPort, service), null);
        }
    }

    protected void notifyResponseListener(final HttpContext context) {
        if (respListenerSup.isNotEmpty()) {
            respListenerSup.forEach(s -> s.onResponse(context, dataPort), null);
        }
    }


}
