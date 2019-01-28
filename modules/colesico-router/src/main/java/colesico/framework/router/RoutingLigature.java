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

package colesico.framework.router;

import colesico.framework.teleapi.TeleMethod;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Vladlen Larionov
 */
public final class RoutingLigature {

    public static final String ADD_METHOD = "add";
    public static final String ROUTE_PARAM = "route";
    public static final String TELE_METHOD_PARAM = "teleMethod";
    public static final String ORIGIN_METHOD_PARAM = "originMethod";

    private final Class<?> framletClass;
    private final Map<String, RouteInfo> routesMap = new TreeMap<>();

    public RoutingLigature(Class<?> framletClass) {
        this.framletClass = framletClass;
    }

    public void add(String route, TeleMethod teleMethod, String teleMethodName) {
        RouteInfo routeInfo = new RouteInfo(route, teleMethod, teleMethodName);
        RouteInfo oldRouteInfo = routesMap.put(route, routeInfo);
        if (oldRouteInfo != null) {
            throw new RouterException("Duplicate route: " + route + " -> " + routeInfo + " | " + oldRouteInfo);
        }
    }

    public Class<?> getFramletClass() {
        return framletClass;
    }

    public Collection<RouteInfo> getRoutesIno() {
        return routesMap.values();
    }

    public static final class RouteInfo {
        private final String route;
        private final TeleMethod teleMethod;
        private final String teleMethodName;

        public RouteInfo(String route, TeleMethod teleMethod, String teleMethodName) {
            this.route = route;
            this.teleMethod = teleMethod;
            this.teleMethodName = teleMethodName;
        }

        public String getRoute() {
            return route;
        }

        public TeleMethod getTeleMethod() {
            return teleMethod;
        }

        public String getTeleMethodName() {
            return teleMethodName;
        }

        @Override
        public String toString() {
            return "RouteInfo{" +
                    "route='" + route + '\'' +
                    ", originMethod='" + teleMethodName + '\'' +
                    '}';
        }
    }

}