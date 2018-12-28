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

package colesico.framework.router.codegen;


import colesico.framework.http.HttpMethod;
import colesico.framework.assist.Elements;
import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
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

    protected static final String INDEX_FRAMLET_NAME = "Index";

    protected final Elements<RoutedTeleMethodElement> teleMethods = new Elements<>();

    protected final String framletRoute;

    public RoutegenContext(ServiceElement framlet) {
        this.framletRoute = buildFramletRoute(framlet);
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

        Route routeAnnotation = teleMethod.getProxyMethod().getOriginMethod().getAnnotation(Route.class);
        String methodRoute;
        if (routeAnnotation == null) {
            methodRoute = RouteTrie.SEGMENT_DELEMITER + StrUtils.toLowerCaseNotation(teleMethod.getName());
        } else {
            methodRoute = routeAnnotation.value();
        }

        if (methodRoute.equals(RouteTrie.SEGMENT_DELEMITER)) {
            methodRoute = "";
        }

        HttpMethod httpMethod = HttpMethod.GET;
        RequestMethod methodAnnotation = teleMethod.getProxyMethod().getOriginMethod().getAnnotation(RequestMethod.class);
        if (methodAnnotation != null) {
            httpMethod = methodAnnotation.value();
        }

        String result = StrUtils.concatPath(httpMethod.name(), framletRoute, RouteTrie.SEGMENT_DELEMITER);
        result = StrUtils.concatPath(result, methodRoute, RouteTrie.SEGMENT_DELEMITER);

        return result;
    }

    protected String buildFramletRoute(ServiceElement framlet) {
        Route routeAnnotation = framlet.getOriginClass().getAnnotation(Route.class);
        String route = null;
        if (routeAnnotation != null) {
            // If absolute route
            if (routeAnnotation.value().startsWith(RouteTrie.SEGMENT_DELEMITER)) {
                return routeAnnotation.value();
            }
            // Relative route -> get suffix from package
            if (routeAnnotation.value().startsWith(".")) {
                PackageElement pkg = CodegenUtils.getPackage(framlet.getOriginClass());
                Route pkgRoute = pkg.getAnnotation(Route.class);
                if (pkgRoute == null) {
                    throw CodegenException.of()
                            .message("Package " + pkg.getSimpleName() + " must be annotated with @" + Route.class.getName())
                            .element(framlet.getOriginClass())
                            .build();
                }
                String pkgRouteValue = pkgRoute.value();
                if (!pkgRouteValue.startsWith(RouteTrie.SEGMENT_DELEMITER)) {
                    throw CodegenException.of()
                            .message("Wrong package route: " + pkgRouteValue + ". Must starts with '" + RouteTrie.SEGMENT_DELEMITER + "'")
                            .element(framlet.getOriginClass())
                            .build();
                }
                String beanRouteValue = routeAnnotation.value().substring(1);
                if (StringUtils.isBlank(beanRouteValue)){
                    String classSimpleName = framlet.getOriginClass().getSimpleName().toString();
                    beanRouteValue = "/"+ StrUtils.toLowerCaseNotation(classSimpleName);
                }
                return StrUtils.concatPath(pkgRouteValue, beanRouteValue, RouteTrie.SEGMENT_DELEMITER);
            } else {
                throw CodegenException.of()
                        .message("Unclear route: " + routeAnnotation.value() + ". Must starts with '.' or '" + RouteTrie.SEGMENT_DELEMITER + "'")
                        .element(framlet.getOriginClass())
                        .build();
            }
        } else {
            // Root suffix
            if (framlet.getOriginClass().getSimpleName().toString().equals(INDEX_FRAMLET_NAME)) {
                return "/";
            } else {
                String classSimpleName = framlet.getOriginClass().getSimpleName().toString();
                return "/" + StrUtils.toLowerCaseNotation(classSimpleName);
            }
        }
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
