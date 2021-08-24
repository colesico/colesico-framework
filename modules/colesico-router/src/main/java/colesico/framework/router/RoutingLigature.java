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

package colesico.framework.router;

import colesico.framework.teleapi.TeleMethod;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Binds routes with route action methods  (tele-facade methods)
 */
public final class RoutingLigature {

    public static final String ADD_METHOD = "add";
    public static final String ROUTE_PARAM = "route";
    public static final String TELE_METHOD_PARAM = "teleMethod";
    public static final String TARGET_METHOD_PARAM = "targetMethod";
    public static final String ATTRIBUTES_PARAM = "attributes";

    private final Class<?> targetClass;

    private final Map<String, RouteInfo> routesMap = new TreeMap<>();

    public RoutingLigature(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * Add route to ligature
     *
     * @param route        route definition with http method (ex: GET/my/foo )
     * @param teleMethod   action handler
     * @param targetMethod handler method name
     * @param attributes   route attributes (see {@link RouteAttribute})
     */
    public void add(String route, TeleMethod teleMethod, String targetMethod, Map<String, String> attributes) {
        RouteInfo routeInfo = new RouteInfo(route, teleMethod, targetMethod, attributes);
        RouteInfo oldRouteInfo = routesMap.put(route, routeInfo);
        if (oldRouteInfo != null) {
            throw new RouterException("Duplicate route: " + route + " -> " + routeInfo + " | " + oldRouteInfo);
        }
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Collection<RouteInfo> getRoutesInfo() {
        return routesMap.values();
    }

    public static final class RouteInfo {

        /**
         * Route with http method
         */
        private final String route;

        /**
         * Action handler method
         */
        private final TeleMethod teleMethod;

        /**
         * Target action handler method name
         */
        private final String targetMethod;

        /**
         * Route attributes
         *
         * @see RouteAttribute
         */
        private final Map<String, String> attributes;

        public RouteInfo(String route,
                         TeleMethod teleMethod,
                         String targetMethod,
                         Map<String, String> attributes) {

            this.route = route;
            this.teleMethod = teleMethod;
            this.targetMethod = targetMethod;
            this.attributes = attributes;
        }

        public String getRoute() {
            return route;
        }

        public TeleMethod getTeleMethod() {
            return teleMethod;
        }

        public String getTargetMethod() {
            return targetMethod;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }

        @Override
        public String toString() {
            return "RouteInfo{" +
                    "route='" + route + '\'' +
                    ", targetMethod='" + targetMethod + '\'' +
                    '}';
        }
    }

}
