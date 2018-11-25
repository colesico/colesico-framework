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

package colesico.framework.weblet.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.ioc.ThreadScope;
import colesico.framework.router.RouterContext;
import colesico.framework.security.PrincipalRequiredException;
import colesico.framework.teleapi.DataPort;
import colesico.framework.weblet.assist.CSRFProtector;
import colesico.framework.weblet.teleapi.Authenticator;
import colesico.framework.weblet.teleapi.InvokeContext;
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

    @Inject
    public WebletTeleDriverImpl(ThreadScope threadScope, WebletDataPortImpl dataPort, Provider<Authenticator> authenticatorProv, Provider<HttpContext> httpContextProv, Provider<RouterContext> routerContextProv) {
        this.threadScope = threadScope;
        this.dataPort = dataPort;
        this.authenticatorProv = authenticatorProv;
        this.httpContextProv = httpContextProv;
        this.routerContextProv = routerContextProv;
    }

    @Override
    public <T> void invoke(T target, Binder<T, WebletDataPort> binder, InvokeContext context) {
        try {
            threadScope.put(DataPort.SCOPE_KEY, dataPort);
            HttpRequest request = httpContextProv.get().getRequest();
            CSRFProtector.check(request);
            binder.invoke(target, dataPort);
        } catch (PrincipalRequiredException pre) {
            if (authenticatorProv.get().authenticate()) {
                binder.invoke(target, dataPort);
            }
        }
    }
}
