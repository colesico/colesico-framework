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
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.Router;
import colesico.framework.router.RouterContext;
import colesico.framework.router.RouterDescriptors;
import colesico.framework.router.RouterInvocation;
import colesico.framework.security.PrincipalRequiredException;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.telehttp.assist.CSRFProtector;
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

    protected final ThreadScope threadScope;
    protected final WebletDataPortImpl dataPort;
    protected final Provider<Authenticator> authenticatorProv;
    protected final Provider<HttpContext> httpContextProv;
    protected final Provider<RouterContext> routerContextProv;
    protected final CSRFProtector csrfProtector;

    public WebletTeleControllerImpl(ThreadScope threadScope, WebletDataPortImpl dataPort, Provider<Authenticator> authenticatorProv, Provider<HttpContext> httpContextProv, Provider<RouterContext> routerContextProv, CSRFProtector csrfProtector) {
        this.threadScope = threadScope;
        this.dataPort = dataPort;
        this.authenticatorProv = authenticatorProv;
        this.httpContextProv = httpContextProv;
        this.routerContextProv = routerContextProv;
        this.csrfProtector = csrfProtector;
    }

    @Override
    public Polysupplier<TeleFacade<?, RouterDescriptors>> teleFacades() {
        return null;
    }

    @Override
    public Optional<RouterInvocation> resolve(Router.Criteria criteria) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void perform(RouterInvocation invocation) {
        try {
            threadScope.put(DataPort.SCOPE_KEY, dataPort);
            HttpRequest request = httpContextProv.get().request();
            csrfProtector.check(request);
            invoker.invoke(target, dataPort);
        } catch (PrincipalRequiredException pre) {
            if (authenticatorProv.get().authenticate()) {
                invoker.invoke(target, dataPort);
            } else {
                throw pre;
            }
        }
    }

    @Override
    public void register(TeleFacade<?, RouterDescriptors> teleFacade) {
        throw new UnsupportedOperationException("Not supported");
    }
}
