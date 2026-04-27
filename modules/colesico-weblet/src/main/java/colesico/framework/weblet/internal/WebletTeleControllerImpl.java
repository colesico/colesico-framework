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
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.Router;
import colesico.framework.router.RouterContext;
import colesico.framework.router.RouterCommands;
import colesico.framework.security.PrincipalRequiredException;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.telehttp.assist.CSRFProtector;
import colesico.framework.weblet.Weblet;
import colesico.framework.weblet.teleapi.Authenticator;
import colesico.framework.weblet.teleapi.WebletTeleController;
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

    protected final WebletDataPortImpl dataPort;
    protected final Polysupplier<TeleFacade<?, RouterCommands>> teleFacades;

    protected final Provider<HttpContext> httpContextProv;
    protected final Provider<RouterContext> routerContextProv;

    protected final Provider<Authenticator> authenticatorProv;

    protected final CSRFProtector csrfProtector;

    protected final ThreadScope threadScope;

    public WebletTeleControllerImpl(WebletDataPortImpl dataPort,
                                    @Classed(Weblet.class)
                                    Polysupplier<TeleFacade> teleFacades,
                                    Provider<HttpContext> httpContextProv,
                                    Provider<RouterContext> routerContextProv,
                                    Provider<Authenticator> authenticatorProv,
                                    CSRFProtector csrfProtector,
                                    ThreadScope threadScope) {
        this.dataPort = dataPort;
        this.teleFacades = (Polysupplier) teleFacades;
        this.httpContextProv = httpContextProv;
        this.routerContextProv = routerContextProv;
        this.authenticatorProv = authenticatorProv;
        this.csrfProtector = csrfProtector;
        this.threadScope = threadScope;
    }

    @Override
    public Iterable<TeleFacade<?, RouterCommands>> teleFacades() {
        return teleFacades;
    }

    @Override
    public Optional<Router.Invocation> resolve(Object criteria) {
        return Optional.empty();
    }

    @Override
    public void execute(Router.Invocation invocation) {
        try {
            threadScope.put(DataPort.SCOPE_KEY, dataPort);
            HttpRequest request = httpContextProv.get().request();
            csrfProtector.check(request);
            invocation.action().teleCommand().execute(dataPort);
        } catch (PrincipalRequiredException pre) {
            if (authenticatorProv.get().authenticate()) {
                invocation.action().teleCommand().execute(dataPort);
            } else {
                throw pre;
            }
        }
    }

    @Override
    public void register(TeleFacade<?, RouterCommands> teleFacade) {
        throw new UnsupportedOperationException("Not supported");
    }
}
