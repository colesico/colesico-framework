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

package colesico.framework.router;

import colesico.framework.teleapi.TeleCommand;
import colesico.framework.teleapi.TeleFacade;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Binds routes with route actions  (tele-facade methods)
 */
public final class RouterCommandsRegistry implements TeleFacade.CommandsRegistry {

    public static final String ADD_METHOD = "add";
    public static final String ROUTE_PARAM = "route";
    public static final String TELE_METHOD_PARAM = "teleCommand";
    public static final String TARGET_METHOD_PARAM = "targetMethod";
    public static final String ATTRIBUTES_PARAM = "attributes";

    /**
     * The class whose method will be called via tele-api
     * Usually a service class ({@link colesico.framework.service.Service})
     */
    private final Class<?> targetClass;

    /**
     * route -> entry
     */
    private final Map<String, RouteEntry> routesMap = new TreeMap<>();

    public RouterCommandsRegistry(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * Add route to this commands registry
     *
     * @param route        route definition with http method (ex: GET/my/foo )
     * @param teleCommand  action handler
     * @param targetMethod handler method name
     * @param attributes   route attributes (see {@link RouteAttribute})
     */
    public void add(String route, TeleCommand teleCommand, String targetMethod, Map<String, String> attributes) {
        RouteEntry routeEntry = new RouteEntry(route, teleCommand, targetMethod, attributes);
        RouteEntry oldRouteEntry = routesMap.put(route, routeEntry);
        if (oldRouteEntry != null) {
            throw new RouterException("Duplicate route: " + route + " -> " + routeEntry + " | " + oldRouteEntry);
        }
    }

    public Class<?> targetClass() {
        return targetClass;
    }

    public Collection<RouteEntry> entries() {
        return routesMap.values();
    }

    /**
     * @param route        Route with http method
     * @param teleCommand  Tele-command to execute
     * @param targetMethod Target method name
     * @param attributes   Route attributes, see {@link RouteAttribute}
     */
    public record RouteEntry(String route, TeleCommand teleCommand, String targetMethod,
                             Map<String, String> attributes) {

        @Override
        public String toString() {
            return "RouteInfo{" +
                    "route='" + route + '\'' +
                    ", targetMethod='" + targetMethod + '\'' +
                    '}';
        }
    }

}
