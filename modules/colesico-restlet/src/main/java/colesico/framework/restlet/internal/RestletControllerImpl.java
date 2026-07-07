/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.restlet.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.restlet.*;
import colesico.framework.router.Router;
import colesico.framework.router.RouterCommandsRegistry;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.telehttp.assist.CSRFProtector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.Optional;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class RestletControllerImpl implements RestletController {

    public static final String X_REQUESTED_WITH_HEADER = "X-Requested-With";
    public static final String X_REQUESTED_WITH_HEADER_VAL = "XMLHttpRequest";

    protected final Logger logger = LoggerFactory.getLogger(RestletController.class);

    protected final RestletConfigPrototype config;

    protected final TaskScope taskScope;

    protected final Provider<HttpContext> httpContext;

    protected final RestletDataPort dataPort;

    protected final Polysupplier<TeleFacade<?, RouterCommandsRegistry>> teleFacades;

    protected final CSRFProtector csrfProtector;

    protected final Polysupplier<RestletRequestListener> reqListenerSup;

    protected final Polysupplier<RestletResponseListener> respListenerSup;

    @Inject
    public RestletControllerImpl(
            RestletConfigPrototype config,
            TaskScope taskScope,
            Provider<HttpContext> httpContext,
            RestletDataPort dataPort,
            @Classed(Restlet.class)
            Polysupplier<TeleFacade> teleFacades,
            CSRFProtector csrfProtector,
            Polysupplier<RestletRequestListener> reqListenerSup,
            Polysupplier<RestletResponseListener> respListenerSup) {

        logger.info("Init restlet controller...");

        this.config = config;
        this.taskScope = taskScope;
        this.httpContext = httpContext;
        this.dataPort = dataPort;
        this.teleFacades = (Polysupplier) teleFacades;
        this.csrfProtector = csrfProtector;
        this.reqListenerSup = reqListenerSup;
        this.respListenerSup = respListenerSup;
    }

    @Override
    public Iterable<TeleFacade<?, RouterCommandsRegistry>> teleFacades() {
        return teleFacades;
    }

    @Override
    public Optional<Router.Invocation> resolve(Criteria criteria) {
        return Optional.empty();
    }

    @Override
    public void register(TeleFacade<?, RouterCommandsRegistry> teleFacade) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void execute(Router.Invocation invocation) {

        // Set data port to  be accessible
        taskScope.put(DataPort.SCOPE_KEY, dataPort);
        // Retrieve http context
        HttpContext httpContext = this.httpContext.get();
        // Retrieve http request
        HttpRequest httpRequest = httpContext.request();
        // Request listener notification
        notifyRequestListener(httpContext, invocation);

        // CSRF protection
        if (config.csrfProtection()) {
            csrfProtector.check(httpRequest);
        }

        // Invoke tele-command
        invocation.action().teleCommand().execute();

        notifyResponseListener(httpContext);
    }

    protected void notifyRequestListener(final HttpContext context, Router.Invocation invocation) {
        if (reqListenerSup.isNotEmpty()) {
            reqListenerSup.forEach(s -> s.onRequest(context, dataPort, invocation));
        }
    }

    protected void notifyResponseListener(final HttpContext context) {
        if (respListenerSup.isNotEmpty()) {
            respListenerSup.forEach(s -> s.onResponse(context, dataPort));
        }
    }

}
