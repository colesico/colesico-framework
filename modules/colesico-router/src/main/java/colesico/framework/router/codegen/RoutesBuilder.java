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

package colesico.framework.router.codegen;


import colesico.framework.assist.Elements;
import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ParserElement;
import colesico.framework.http.HttpMethod;
import colesico.framework.router.RequestMethod;
import colesico.framework.router.Route;
import colesico.framework.router.RouteAttribute;
import colesico.framework.router.RouteAttributes;
import colesico.framework.router.assist.RouteTrie;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.teleapi.TeleMethodElement;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.PackageElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Routes builder
 * Used to construct routes while a service parsing
 */
public class RoutesBuilder {

    protected static final String INDEX_SERVICE_PREFIX = "Index";
    protected static final String INDEX_METHOD_NAME = "index";
    protected static final String OTHER_METHOD_NAME = "other";

    protected final Elements<RoutedTeleMethodElement> teleMethods = new Elements<>();

    protected final String serviceRoute;

    public RoutesBuilder(ServiceElement service) {
        this.serviceRoute = buildServiceRoute(service);
    }

    public final void addTeleMethod(TeleMethodElement teleMethod) {
        String targetMethodName = teleMethod.getName();
        final String route = buildMethodRoute(teleMethod);

        RoutedTeleMethodElement rtme = teleMethods.find(rte -> rte.getRoute().equals(route));
        if (null != rtme) {
            throw CodegenException.of()
                    .message("Duplicate router path: " + route + "->" + targetMethodName + "(...). Route already bound to " + rtme.getTeleMethod().getServiceMethod().getName() + "(...)")
                    .element(teleMethod.getServiceMethod().getOriginMethod()).build();
        }

        Map<String, String> methodRouteAttrs = parseRouteAttributes(teleMethod.getServiceMethod().getOriginMethod());
        Map<String, String> classRouteAttrs = parseRouteAttributes(teleMethod.getServiceMethod().getOriginMethod().getParentClass());
        classRouteAttrs.putAll(methodRouteAttrs);

        RoutedTeleMethodElement routedTeleMethod = new RoutedTeleMethodElement(teleMethod, route, classRouteAttrs);
        teleMethods.add(routedTeleMethod);
        teleMethod.setProperty(RoutedTeleMethodElement.class, routedTeleMethod);
    }

    protected String buildMethodRoute(TeleMethodElement teleMethod) {

        AnnotationAssist<Route> routeAnn = teleMethod.getServiceMethod().getOriginMethod().getAnnotation(Route.class);
        String methodRoute;
        if (routeAnn != null) {
            methodRoute = StringUtils.trim(routeAnn.unwrap().value());
            // If NOT absolute route
            if (!methodRoute.startsWith(RouteTrie.SEGMENT_DELEMITER)) {
                // Local route optional marker
                if (methodRoute.startsWith("./")) {
                    methodRoute = methodRoute.substring(2);
                }
                methodRoute = StrUtils.concatPath(serviceRoute, methodRoute, RouteTrie.SEGMENT_DELEMITER);
            }
        } else {
            String methodName = teleMethod.getName();
            if (methodName.equals(INDEX_METHOD_NAME)) {
                // Local root route
                methodRoute = "";
            } else if (methodName.equals(OTHER_METHOD_NAME)) {
                // any route
                methodRoute = "*";
            } else {
                methodRoute = StrUtils.toSeparatorNotation(methodName, '-');
            }
            methodRoute = StrUtils.concatPath(serviceRoute, methodRoute, RouteTrie.SEGMENT_DELEMITER);
        }

        HttpMethod httpMethod = HttpMethod.HTTP_METHOD_GET;
        AnnotationAssist<RequestMethod> methodAnnotation = teleMethod.getServiceMethod().getOriginMethod().getAnnotation(RequestMethod.class);
        if (methodAnnotation != null) {
            httpMethod = HttpMethod.of(methodAnnotation.unwrap().value());
        }

        return StrUtils.concatPath(httpMethod.getName(), methodRoute, RouteTrie.SEGMENT_DELEMITER);
    }

    protected String buildServiceRoute(ServiceElement service) {
        AnnotationAssist<Route> routeAnn = service.getOriginClass().getAnnotation(Route.class);
        String srvRoute;
        if (routeAnn != null) {
            srvRoute = StringUtils.trim(routeAnn.unwrap().value());

            // If absolute route
            if (srvRoute.startsWith(RouteTrie.SEGMENT_DELEMITER)) {
                return srvRoute;
            }

            // Local route optional marker
            if (srvRoute.startsWith("./")) {
                srvRoute = srvRoute.substring(2);
            }

            String pkgRoute = buildPackageRoute(service.getOriginClass().getPackage());
            return StrUtils.concatPath(pkgRoute, srvRoute, RouteTrie.SEGMENT_DELEMITER);
        } else {
            String serviceBeanName = service.getOriginClass().getSimpleName();
            // Local root route
            if (serviceBeanName.startsWith(INDEX_SERVICE_PREFIX)) {
                srvRoute = "";
            } else {
                // Bean route from bean simple class name
                srvRoute = StrUtils.toSeparatorNotation(serviceBeanName, '-');
            }
            String pkgRoute = buildPackageRoute(service.getOriginClass().getPackage());
            return StrUtils.concatPath(pkgRoute, srvRoute, RouteTrie.SEGMENT_DELEMITER);
        }
    }

    protected String buildPackageRoute(PackageElement pkg) {
        Route routeAnn = pkg.getAnnotation(Route.class);
        if (routeAnn == null) {
            return "/";
        }
        String route = StringUtils.trim(routeAnn.value());
        if (!route.startsWith(RouteTrie.SEGMENT_DELEMITER)) {
            throw CodegenException.of()
                    .message("Wrong package route: " + route + ". must starts with '" + RouteTrie.SEGMENT_DELEMITER + "'")
                    .element(pkg)
                    .build();
        }
        return route;
    }

    protected Map<String, String> parseRouteAttributes(ParserElement methodOrClass) {
        Map<String, String> result = new HashMap<>();
        AnnotationAssist<RouteAttributes> routeAttributesAnn = methodOrClass.getAnnotation(RouteAttributes.class);
        if (routeAttributesAnn == null) {
            AnnotationAssist<RouteAttribute> routeAttrAnn = methodOrClass.getAnnotation(RouteAttribute.class);
            if (routeAttrAnn != null) {
                result.put(routeAttrAnn.unwrap().name(), routeAttrAnn.unwrap().value());
            }
        } else {
            for (RouteAttribute routeAttrAnn : routeAttributesAnn.unwrap().value()) {
                result.put(routeAttrAnn.name(), routeAttrAnn.value());
            }
        }
        return result;
    }

    public final Elements<RoutedTeleMethodElement> getTeleMethods() {
        return teleMethods;
    }

    public static class RoutedTeleMethodElement {
        protected final TeleMethodElement teleMethod;
        protected final String route;
        protected final Map<String, String> routeAttributes;

        public RoutedTeleMethodElement(TeleMethodElement teleMethod, String route, Map<String, String> routeAttributes) {
            this.teleMethod = teleMethod;
            this.route = route;
            this.routeAttributes = routeAttributes;
        }

        public final String getRoute() {
            return route;
        }

        public TeleMethodElement getTeleMethod() {
            return teleMethod;
        }

        public Map<String, String> getRouteAttributes() {
            return routeAttributes;
        }

        public String getHttpMethodName() {
            int i = route.indexOf(RouteTrie.SEGMENT_DELEMITER);
            return route.substring(0, i);
        }

        public String getRoutePath() {
            int i = route.indexOf(RouteTrie.SEGMENT_DELEMITER);
            return '/'+route.substring(i + 1);
        }
    }

}
