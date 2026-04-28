/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.*;
import colesico.framework.router.assist.RouteTrie;
import colesico.framework.teleapi.TeleController;
import colesico.framework.teleapi.TeleException;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.TeleCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Router default implementation
 */
public class RouterImpl implements Router {

    protected final Logger log = LoggerFactory.getLogger(Router.class);

    protected final ThreadScope threadScope;

    protected RouteTrie<RouteAction> routeTrie = new RouteTrie<>(null);

    protected RoutesIndex routesIndex = new RoutesIndex();

    @Inject
    public RouterImpl(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    @Override
    public Optional<Router.Invocation> resolve(Criteria criteria) {
        var requestMethod = criteria.requestMethod();
        var requestUri = criteria.requestUri();

        RouteTrie.RouteResolution<RouteAction> routeResolution = routeTrie.resolveRoute(StrUtils.concatPath(requestMethod.name(), requestUri, RouteTrie.SEGMENT_DELEMITER));

        if (routeResolution == null
                || routeResolution.node() == null
                || routeResolution.node().value() == null
                || routeResolution.node().value().teleCommand() == null) {
            return Optional.empty();
        }

        return Optional.of(
                new Router.Invocation(
                        requestMethod,
                        requestUri,
                        routeResolution.node().value(),
                        routeResolution.params())
        );
    }

    @Override
    public void execute(Router.Invocation invocation) {
        if (invocation == null) {
            throw new TeleException("Undetermined invocation target");
        }
        RouterContext routerContext = new RouterContext(invocation.requestUri(), invocation.parameters());
        threadScope.put(RouterContext.SCOPE_KEY, routerContext);

        var teleController = invocation.action().teleController();
        if (teleController == null || teleController == this) {
            //TODO: create default data port
            invocation.action().teleCommand().execute(null);
        } else {
            teleController.execute(invocation);
        }
    }

    @Override
    public List<String> slicedRoute(Class<?> targetClass, String targetMethod, HttpMethod httpMethod, Map<String, String> parameters) {
        return routesIndex.slicedRoute(toRouteId(targetClass, targetMethod, httpMethod), parameters);
    }

    @Override
    public void register(TeleFacade<?, RouterCommands> teleFacade) {
        register(this, teleFacade);
    }

    void register(TeleController<?, Router.Invocation, RouterCommands> teleController,
                  TeleFacade<?, RouterCommands> teleFacade) {
        log.debug("Register http router tele-facade: {}", teleFacade.getClass().getName());

        var commands = teleFacade.commands();

        for (var routeInfo : commands.routesInfo()) {
            log.debug("Route '{}' mapped to target method '{}->{}", routeInfo.route(), commands.targetClass().getName(), routeInfo.targetMethod());
            RouteTrie.Node<RouteAction> node = routeTrie.addRoute(
                    routeInfo.route(),
                    new RouteAction(teleController,
                            routeInfo.teleCommand(),
                            routeInfo.attributes())
            );

            HttpMethod httpMethod = HttpMethod.of(node.root().name());
            routesIndex.addNode(toRouteId(commands.targetClass(), routeInfo.targetMethod(), httpMethod), node);
        }
    }

    void register(RouterTargetController<?> targetController) {
        log.debug("Register http router tele-facades for target controller {}", targetController.getClass().getName());

        for (var teleFacade : targetController.teleFacades()) {
            register(targetController, teleFacade);
        }
    }

    void addCustomAction(HttpMethod httpMethod,
                         String route,
                         TeleController<Router.Criteria, Router.Invocation, RouterCommands> teleController,
                         TeleCommand<?, ?> teleCommand,
                         Class<?> targetClass,
                         String targetMethod,
                         Map<String, String> attributes) {
        String fullRoute = httpMethod.name() + RouteTrie.SEGMENT_DELEMITER + route;
        RouteTrie.Node<RouteAction> node = routeTrie.addRoute(fullRoute, new RouteAction(teleController, teleCommand, attributes));
        routesIndex.addNode(toRouteId(targetClass, targetMethod, httpMethod), node);
        log.debug("Route '{}{}' mapped to custom action method '{}->{}()'", httpMethod.name(), route, targetClass.getName(), targetMethod);
    }

    String toRouteId(Class<?> targetClass, String targetMethod, HttpMethod httpMethod) {
        return targetClass.getName() + ':' + targetMethod + ':' + httpMethod.name();
    }

}
