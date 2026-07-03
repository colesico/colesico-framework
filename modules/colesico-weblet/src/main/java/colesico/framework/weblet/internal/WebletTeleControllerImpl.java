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

package colesico.framework.weblet.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.router.Router;
import colesico.framework.router.RouterCommandsRegistry;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.telehttp.assist.CSRFProtector;
import colesico.framework.weblet.Weblet;
import colesico.framework.weblet.WebletDataPort;
import colesico.framework.weblet.WebletTeleController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.Optional;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class WebletTeleControllerImpl implements WebletTeleController {

    protected final Logger logger = LoggerFactory.getLogger(WebletTeleControllerImpl.class);

    protected final TaskScope taskScope;

    protected final Provider<HttpContext> httpContext;

    protected final WebletDataPort dataPort;

    protected final Polysupplier<TeleFacade<?, RouterCommandsRegistry>> teleFacades;

    protected final CSRFProtector csrfProtector;

    public WebletTeleControllerImpl(
            TaskScope taskScope,
            Provider<HttpContext> httpContext,
            WebletDataPort dataPort,
            @Classed(Weblet.class)
            Polysupplier<TeleFacade> teleFacades,
            CSRFProtector csrfProtector
    ) {
        this.dataPort = dataPort;
        this.teleFacades = (Polysupplier) teleFacades;
        this.httpContext = httpContext;
        this.csrfProtector = csrfProtector;
        this.taskScope = taskScope;
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
    public void execute(Router.Invocation invocation) {
        taskScope.put(DataPort.SCOPE_KEY, dataPort);
        HttpRequest request = httpContext.get().request();
        csrfProtector.check(request);
        invocation.action().teleCommand().execute();
    }

    @Override
    public void register(TeleFacade<?, RouterCommandsRegistry> teleFacade) {
        throw new UnsupportedOperationException("Not supported");
    }
}
