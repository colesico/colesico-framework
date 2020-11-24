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
    public static final String TELE_METHOD_REF_PARAM = "teleMethodRef";
    public static final String ORIGIN_METHOD_PARAM = "originMethod";

    private final Class<?> serviceClass;
    private final Map<String, RouteInfo> routesMap = new TreeMap<>();

    public RoutingLigature(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public void add(String route, TeleMethod teleMethodRef, String targetMethodName, Map<String, String> routeAttributes) {
        RouteInfo routeInfo = new RouteInfo(route, teleMethodRef, targetMethodName, routeAttributes);
        RouteInfo oldRouteInfo = routesMap.put(route, routeInfo);
        if (oldRouteInfo != null) {
            throw new RouterException("Duplicate route: " + route + " -> " + routeInfo + " | " + oldRouteInfo);
        }
    }

    public Class<?> getServiceClass() {
        return serviceClass;
    }

    public Collection<RouteInfo> getRoutesInfo() {
        return routesMap.values();
    }

    public static final class RouteInfo {
        private final String route;
        private final TeleMethod teleMethodRef;
        private final String targetMethodName;
        private final Map<String, String> routeAttributes;

        public RouteInfo(String route,
                         TeleMethod teleMethodRef,
                         String targetMethodName,
                         Map<String, String> routeAttributes) {

            this.route = route;
            this.teleMethodRef = teleMethodRef;
            this.targetMethodName = targetMethodName;
            this.routeAttributes = routeAttributes;
        }

        public String getRoute() {
            return route;
        }

        public TeleMethod getTeleMethodRef() {
            return teleMethodRef;
        }

        public String getTargetMethodName() {
            return targetMethodName;
        }

        public Map<String, String> getRouteAttributes() {
            return routeAttributes;
        }

        @Override
        public String toString() {
            return "RouteInfo{" +
                    "route='" + route + '\'' +
                    ", originMethod='" + targetMethodName + '\'' +
                    '}';
        }
    }

}
