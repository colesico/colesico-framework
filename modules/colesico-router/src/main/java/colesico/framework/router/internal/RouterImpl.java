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

package colesico.framework.router.internal;

import colesico.framework.assist.StrUtils;
import colesico.framework.http.HttpMethod;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.*;
import colesico.framework.router.assist.RouteTrie;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.TeleMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Router default implementation
 */
public class RouterImpl implements Router {

    protected final Logger log = LoggerFactory.getLogger(Router.class);

    protected final ThreadScope threadScope;

    protected RouteTrie<RouteAction> routeTrie;
    protected RoutesIndex routesIndex;

    @Inject
    public RouterImpl(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    protected void loadRoutesMapping(Polysupplier<TeleFacade> teleFacadeSupp) {
        log.debug("Lookup http router tele-facades... ");

        routeTrie = new RouteTrie<>(null);
        routesIndex = new RoutesIndex();

        Iterator<TeleFacade> it = teleFacadeSupp.iterator(null);
        while (it.hasNext()) {
            TeleFacade teleFacade = it.next();
            log.debug("Found http router tele-facade: " + teleFacade.getClass().getName());
            RoutingLigature ligature = (RoutingLigature) teleFacade.getLigature();

            for (RoutingLigature.RouteInfo routeInfo : ligature.getRoutesInfo()) {
                if (log.isDebugEnabled()) {
                    log.debug("Route '" + routeInfo.getRoute() + "' mapped to target method '" +
                            ligature.getTargetClass().getName() + "->" + routeInfo.getTargetMethod());

                }
                RouteTrie.Node<RouteAction> node = routeTrie.addRoute(
                        routeInfo.getRoute(),
                        new RouteAction(routeInfo.getTeleMethod(), routeInfo.getAttributes())
                );

                HttpMethod httpMethod = HttpMethod.of(node.getRoot().getName());
                routesIndex.addNode(toRouteId(ligature.getTargetClass(), routeInfo.getTargetMethod(), httpMethod), node);
            }
        }
    }

    protected void addCustomAction(HttpMethod httpMethod, String route, TeleMethod teleMethod, Class<?> targetClass, String targetMethod, Map<String, String> routeAttributes) {
        String fullRoute = httpMethod.getName() + RouteTrie.SEGMENT_DELEMITER + route;
        RouteTrie.Node<RouteAction> node = routeTrie.addRoute(fullRoute, new RouteAction(teleMethod, routeAttributes));
        routesIndex.addNode(toRouteId(targetClass, targetMethod, httpMethod), node);
        if (log.isDebugEnabled()) {
            log.debug("Route '" + httpMethod.getName() + route + "' mapped to custom action method '" +
                    targetClass.getName() + "->" + targetMethod + "()");

        }
    }

    protected String toRouteId(Class<?> targetClass, String targetMethod, HttpMethod httpMethod) {
        return targetClass.getName() + ':' + targetMethod + ':' + httpMethod.getName();
    }

    @Override
    public List<String> getSlicedRoute(Class<?> targetClass, String targetMethod, HttpMethod httpMethod, Map<String, String> parameters) {
        return routesIndex.getSlicedRoute(toRouteId(targetClass, targetMethod, httpMethod), parameters);
    }

    @Override
    public ActionResolution resolveAction(HttpMethod requestMethod, String requestUri) {
        RouteTrie.RouteResolution<RouteAction> routeResolution = routeTrie.resolveRoute(StrUtils.concatPath(requestMethod.getName(), requestUri, RouteTrie.SEGMENT_DELEMITER));

        if (routeResolution == null
                || routeResolution.getNode() == null
                || routeResolution.getNode().getValue() == null
                || routeResolution.getNode().getValue().getTeleMethod() == null) {
            throw new UnknownRouteException(requestMethod, requestUri);
        }

        return new ActionResolution(requestMethod,
                requestUri,
                routeResolution.getNode().getValue(),
                routeResolution.getParams());
    }

    @Override
    public void performAction(ActionResolution resolution) {
        RouterContext routerContext = new RouterContext(resolution.getRequestUri(), resolution.getRouteParameters());
        threadScope.put(RouterContext.SCOPE_KEY, routerContext);

        TeleMethod teleMethod = resolution.getRouteAction().getTeleMethod();
        teleMethod.invoke();
    }

}
