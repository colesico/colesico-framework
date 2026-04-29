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
import colesico.framework.service.codegen.model.teleapi.TeleCommandElement;
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

    protected final Elements<RoutedTeleCommandElement> teleCommands = new Elements<>();

    protected final String serviceRoute;

    public RoutesBuilder(ServiceElement service) {
        this.serviceRoute = buildServiceRoute(service);
    }

    public final void addTeleCommand(TeleCommandElement teleCommand) {
        String targetMethodName = teleCommand.targetMethodName();
        final String route = buildMethodRoute(teleCommand);

        RoutedTeleCommandElement rtme = teleCommands.find(rte -> rte.route().equals(route));
        if (null != rtme) {
            throw CodegenException.of()
                    .message("Duplicate router path: " + route + "->" + targetMethodName + "(...). Route already bound to " + rtme.teleCommand().serviceMethod().name() + "(...)")
                    .element(teleCommand.serviceMethod().originMethod()).build();
        }

        Map<String, String> methodRouteAttrs = parseRouteAttributes(teleCommand.serviceMethod().originMethod());
        Map<String, String> classRouteAttrs = parseRouteAttributes(teleCommand.serviceMethod().originMethod().parentClass());
        classRouteAttrs.putAll(methodRouteAttrs);

        RoutedTeleCommandElement routedTeleCommand = new RoutedTeleCommandElement(teleCommand, route, classRouteAttrs);
        teleCommands.add(routedTeleCommand);
        teleCommand.setProperty(RoutedTeleCommandElement.class, routedTeleCommand);
    }

    protected String buildMethodRoute(TeleCommandElement teleCommand) {

        AnnotationAssist<Route> routeAnn = teleCommand.serviceMethod().originMethod().annotation(Route.class);
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
            String methodName = teleCommand.targetMethodName();
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
        AnnotationAssist<RequestMethod> methodAnnotation = teleCommand.serviceMethod().originMethod().annotation(RequestMethod.class);
        if (methodAnnotation != null) {
            httpMethod = HttpMethod.of(methodAnnotation.unwrap().value());
        }

        return StrUtils.concatPath(httpMethod.name(), methodRoute, RouteTrie.SEGMENT_DELEMITER);
    }

    protected String buildServiceRoute(ServiceElement service) {
        AnnotationAssist<Route> routeAnn = service.originClass().annotation(Route.class);
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

            String pkgRoute = buildPackageRoute(service.originClass().packageElm());
            return StrUtils.concatPath(pkgRoute, srvRoute, RouteTrie.SEGMENT_DELEMITER);
        } else {
            String serviceBeanName = service.originClass().simpleName();
            // Local root route
            if (serviceBeanName.startsWith(INDEX_SERVICE_PREFIX)) {
                srvRoute = "";
            } else {
                // Bean route from bean simple class name
                srvRoute = StrUtils.toSeparatorNotation(serviceBeanName, '-');
            }
            String pkgRoute = buildPackageRoute(service.originClass().packageElm());
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
        AnnotationAssist<RouteAttributes> routeAttributesAnn = methodOrClass.annotation(RouteAttributes.class);
        if (routeAttributesAnn == null) {
            AnnotationAssist<RouteAttribute> routeAttrAnn = methodOrClass.annotation(RouteAttribute.class);
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

    public final Elements<RoutedTeleCommandElement> teleCommands() {
        return teleCommands;
    }

    public static class RoutedTeleCommandElement {
        protected final TeleCommandElement teleCommand;
        protected final String route;
        protected final Map<String, String> routeAttributes;

        public RoutedTeleCommandElement(TeleCommandElement teleCommand, String route, Map<String, String> routeAttributes) {
            this.teleCommand = teleCommand;
            this.route = route;
            this.routeAttributes = routeAttributes;
        }

        public final String route() {
            return route;
        }

        public TeleCommandElement teleCommand() {
            return teleCommand;
        }

        public Map<String, String> routeAttributes() {
            return routeAttributes;
        }

        public String httpMethodName() {
            int i = route.indexOf(RouteTrie.SEGMENT_DELEMITER);
            return route.substring(0, i);
        }

        public String routePath() {
            int i = route.indexOf(RouteTrie.SEGMENT_DELEMITER);
            return '/'+route.substring(i + 1);
        }
    }

}
