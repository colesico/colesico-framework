/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.router.codegen;


import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.http.HttpMethod;
import colesico.framework.assist.Elements;
import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.router.RequestMethod;
import colesico.framework.router.Route;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.router.assist.RouteTrie;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.PackageElement;

/**
 * @author Vladlen Larionov
 */
abstract public class RoutegenContext {

    protected static final String INDEX_SERVICE_PREFIX = "Index";
    protected static final String INDEX_METHOD_NAME = "index";
    protected static final String OTHER_METHOD_NAME = "other";

    protected final Elements<RoutedTeleMethodElement> teleMethods = new Elements<>();

    protected final String serviceRoute;

    public RoutegenContext(ServiceElement service) {
        this.serviceRoute = buildServiceRoute(service);
    }

    public final void registTeleMethod(TeleMethodElement teleMethod) {
        String teleMethodName = teleMethod.getName();
        final String route = buildMethodRoute(teleMethod);

        RoutedTeleMethodElement rtme = teleMethods.find(rte -> rte.getRoute().equals(route));
        if (null != rtme) {
            throw CodegenException.of()
                .message("Duplicate router path: " + route + "->" + teleMethodName + "(...). Route already binded to " + rtme.getTeleMethod().getProxyMethod().getName() + "(...)")
                .element(teleMethod.getProxyMethod().getOriginMethod()).build();
        }
        RoutedTeleMethodElement routedTeleMethodElement = new RoutedTeleMethodElement(teleMethod, route);
        teleMethods.add(routedTeleMethodElement);
    }

    protected String buildMethodRoute(TeleMethodElement teleMethod) {

        AnnotationElement<Route> routeAnn = teleMethod.getProxyMethod().getOriginMethod().getAnnotation(Route.class);
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
        AnnotationElement<RequestMethod> methodAnnotation = teleMethod.getProxyMethod().getOriginMethod().getAnnotation(RequestMethod.class);
        if (methodAnnotation != null) {
            httpMethod = HttpMethod.of(methodAnnotation.unwrap().value());
        }

        return StrUtils.concatPath(httpMethod.getName(), methodRoute, RouteTrie.SEGMENT_DELEMITER);
    }

    protected String buildServiceRoute(ServiceElement service) {
        AnnotationElement<Route> routeAnn = service.getOriginClass().getAnnotation(Route.class);
        String srvRoute = null;
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
                .message("Wrong package route: " + route + ". Must starts with '" + RouteTrie.SEGMENT_DELEMITER + "'")
                .element(pkg)
                .build();
        }
        return route;
    }

    public final Elements<RoutedTeleMethodElement> getTeleMethods() {
        return teleMethods;
    }

    public static class RoutedTeleMethodElement {
        protected final TeleMethodElement teleMethod;
        protected final String route;


        public RoutedTeleMethodElement(TeleMethodElement teleMethod, String route) {
            this.teleMethod = teleMethod;
            this.route = route;
        }

        public final String getRoute() {
            return route;
        }

        public TeleMethodElement getTeleMethod() {
            return teleMethod;
        }
    }

}
