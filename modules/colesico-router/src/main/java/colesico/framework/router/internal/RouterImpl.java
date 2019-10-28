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

package colesico.framework.router.internal;

import colesico.framework.http.HttpMethod;
import colesico.framework.assist.StrUtils;
import colesico.framework.ioc.*;
import colesico.framework.router.Router;
import colesico.framework.router.RouterContext;
import colesico.framework.router.RoutingLigature;
import colesico.framework.router.UnknownRouteException;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.TeleMethod;
import colesico.framework.router.assist.RouteTrie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class RouterImpl implements Router {

    protected final Logger log = LoggerFactory.getLogger(Router.class);

    protected final ThreadScope threadScope;

    protected RouteTrie<TeleMethod> routeTrie;
    protected RoutesIndex routesIndex;

    @Inject
    public RouterImpl(
        @InlineInject
        @Classed(Router.class)
            Polysupplier<TeleFacade> teleFacadesSupp,
        ThreadScope threadScope) {
        this.threadScope = threadScope;
        loadRoutes(teleFacadesSupp);
    }

    protected void loadRoutes(Polysupplier<TeleFacade> teleFacadeSupp) {
        log.debug("Lookup routing tele-facades... ");

        routeTrie = new RouteTrie<>(null);
        routesIndex = new RoutesIndex();

        Iterator<TeleFacade> it = teleFacadeSupp.iterator(null);
        while (it.hasNext()) {
            TeleFacade teleFacade = it.next();
            log.debug("Found routing tele-facade: " + teleFacade.getClass().getName());
            RoutingLigature ligature = (RoutingLigature) teleFacade.getLigature();

            for (RoutingLigature.RouteInfo routeInfo : ligature.getRoutesIno()) {
                if (log.isDebugEnabled()) {
                    log.debug("Route '" + routeInfo.getRoute() + "' mapped to tele-method '" +
                        ligature.getServiceClass().getName() + "->" + routeInfo.getTeleMethodName());

                }
                RouteTrie.Node<TeleMethod> node = routeTrie.addRoute(
                    routeInfo.getRoute(),
                    routeInfo.getTeleMethod()
                );

                HttpMethod httpMethod = HttpMethod.of(node.getRoot().getName());
                routesIndex.addNode(toRouteId(ligature.getServiceClass(), routeInfo.getTeleMethodName(), httpMethod), node);
            }
        }
    }

    protected String toRouteId(Class<?> serviceClass, String teleMethodName, HttpMethod httpMethod) {
        return serviceClass.getName() + ':' + teleMethodName + ':' + httpMethod.getName();
    }

    @Override
    public List<String> getSlicedRoute(Class<?> serviceClass, String teleMethodName, HttpMethod httpMethod, Map<String, String> parameters) {
        return routesIndex.getSlicedRoute(toRouteId(serviceClass, teleMethodName, httpMethod), parameters);
    }

    @Override
    public void perform(HttpMethod httpMethod, String uri) {
        RouteTrie.RouteResolution<TeleMethod> routeResolution = routeTrie.resolveRoute(StrUtils.concatPath(httpMethod.getName(), uri, RouteTrie.SEGMENT_DELEMITER));
        if (routeResolution == null) {
            throw new UnknownRouteException(uri, httpMethod);
        }

        TeleMethod telemethod = routeResolution.getNode().getValue();
        if (telemethod == null) {
            throw new UnknownRouteException(uri, httpMethod);
        }

        RouterContext routerContext = new RouterContext(uri, routeResolution.getParams());
        threadScope.put(RouterContext.SCOPE_KEY, routerContext);

        telemethod.invoke();
    }
}
