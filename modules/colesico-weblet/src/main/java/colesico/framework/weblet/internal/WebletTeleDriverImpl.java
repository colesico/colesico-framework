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

package colesico.framework.weblet.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.RouterContext;
import colesico.framework.security.PrincipalRequiredException;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.MethodInvoker;
import colesico.framework.telehttp.assist.CSRFProtector;
import colesico.framework.weblet.teleapi.Authenticator;
import colesico.framework.weblet.teleapi.WebletTIContext;
import colesico.framework.weblet.teleapi.WebletDataPort;
import colesico.framework.weblet.teleapi.WebletTeleDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class WebletTeleDriverImpl implements WebletTeleDriver {

    protected final Logger logger = LoggerFactory.getLogger(WebletTeleDriverImpl.class);

    protected final ThreadScope threadScope;
    protected final WebletDataPortImpl dataPort;
    protected final Provider<Authenticator> authenticatorProv;
    protected final Provider<HttpContext> httpContextProv;
    protected final Provider<RouterContext> routerContextProv;
    protected final CSRFProtector csrfProtector;

    public WebletTeleDriverImpl(ThreadScope threadScope, WebletDataPortImpl dataPort, Provider<Authenticator> authenticatorProv, Provider<HttpContext> httpContextProv, Provider<RouterContext> routerContextProv, CSRFProtector csrfProtector) {
        this.threadScope = threadScope;
        this.dataPort = dataPort;
        this.authenticatorProv = authenticatorProv;
        this.httpContextProv = httpContextProv;
        this.routerContextProv = routerContextProv;
        this.csrfProtector = csrfProtector;
    }

    @Override
    public <T> void invoke(T target, MethodInvoker<T, WebletDataPort> invoker, WebletTIContext context) {
        try {
            threadScope.put(DataPort.SCOPE_KEY, dataPort);
            HttpRequest request = httpContextProv.get().getRequest();
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
}
