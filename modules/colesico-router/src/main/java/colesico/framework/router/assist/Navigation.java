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
package colesico.framework.router.assist;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.router.ActionResolution;
import colesico.framework.router.Router;
import colesico.framework.router.RouterException;
import colesico.framework.service.ServiceProxy;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Navigation helper
 *
 * @author Vladlen Larionov
 */
public class Navigation<N extends Navigation> {

    protected String uri;
    protected Class<?> serviceClass;
    protected String targetMethod;
    protected HttpMethod httpMethod = HttpMethod.HTTP_METHOD_GET;
    protected int statusCode = 302;
    protected final Map<String, String> queryParameters = new HashMap<>();
    protected final Map<String, String> routeParameters = new HashMap<>();
    protected final Map<String, List<String>> headers = new HashMap<>();
    protected final Set<HttpCookie> cookies = new HashSet<>();

    public static Navigation of() {
        return new Navigation();
    }

    public static Navigation of(String uri) {
        return new Navigation().uri(uri);
    }

    public static Navigation of(Class<?> serviceClass, String methodName) {
        return new Navigation().service(serviceClass).method(methodName);
    }

    public N uri(String uri) {
        this.uri = uri;
        return (N) this;
    }

    public N service(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
        return (N) this;
    }

    public N service(Object serviceInstance) {
        this.serviceClass = ((ServiceProxy) serviceInstance).getServiceOrigin();
        return (N) this;
    }

    public N method(String methodName) {
        this.targetMethod = methodName;
        return (N) this;
    }

    public N httpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return (N) this;
    }

    public N queryParam(String name, String value) {
        if (value != null) {
            queryParameters.put(name, value);
        }
        return (N) this;
    }

    public N queryParam(String name, Character value) {
        if (value != null) {
            queryParameters.put(name, Character.toString(value));
        }
        return (N) this;
    }


    public N queryParam(String name, Long value) {
        if (value != null) {
            queryParameters.put(name, Long.toString(value));
        }
        return (N) this;
    }

    public N queryParam(String name, Integer value) {
        if (value != null) {
            queryParameters.put(name, Integer.toString(value));
        }
        return (N) this;
    }

    public N queryParam(String name, Short value) {
        if (value != null) {
            queryParameters.put(name, Short.toString(value));
        }
        return (N) this;
    }

    public N queryParam(String name, Boolean value) {
        if (value != null) {
            queryParameters.put(name, Boolean.toString(value));
        }
        return (N) this;
    }

    /**
     * Set query parameters
     */
    public N queryParamsMap(Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey() != null) {
                queryParameters.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return (N) this;
    }

    public N routeParam(String name, String value) {
        routeParameters.put(name, value);
        return (N) this;
    }

    public N routeParam(String name, Long value) {
        routeParameters.put(name, Long.toString(value));
        return (N) this;
    }

    public N routeParam(String name, Integer value) {
        routeParameters.put(name, Integer.toString(value));
        return (N) this;
    }

    public N routeParam(String name, Short value) {
        routeParameters.put(name, Short.toString(value));
        return (N) this;
    }

    public N routeParam(String name, Boolean value) {
        routeParameters.put(name, Boolean.toString(value));
        return (N) this;
    }

    /**
     * Set route parameters
     */
    public N routeParamsMap(Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey() != null) {
                routeParameters.put(entry.getKey(), entry.getValue().toString());
            } else {
                throw new RouterException("Empty route parameter value: " + entry);
            }
        }
        return (N) this;
    }

    /**
     * Set custom http redirect status code.
     * Default code 302
     */
    public N statusCode(int code) {
        this.statusCode = code;
        return (N) this;
    }

    /**
     * Set HTTP header
     */
    public N header(String name, String vale) {
        List<String> hValues = headers.computeIfAbsent(name, n -> new ArrayList<>());
        hValues.add(vale);
        return (N) this;
    }

    /**
     * Set HTTP cookie
     */
    public N cookie(HttpCookie cookie) {
        cookies.add(cookie);
        return (N) this;
    }

    /**
     * Returns url to use in http redirect/forward
     */
    public String toLocation(Router router) {
        String targetURI;
        if (StringUtils.isNotBlank(this.uri)) {
            targetURI = uri;
        } else if (this.serviceClass != null && this.targetMethod != null) {
            List<String> slicedRoute = router.getSlicedRoute(this.serviceClass, this.targetMethod, this.httpMethod, this.routeParameters);
            if (slicedRoute == null) {
                throw new NavigationException("Unknown uri for service '" + serviceClass.getName() + "' and method name '" + targetMethod + "'");
            }
            slicedRoute.remove(0);
            targetURI = RouteTrie.SEGMENT_DELEMITER + StringUtils.join(slicedRoute, RouteTrie.SEGMENT_DELEMITER);
        } else {
            throw new NavigationException("Location URI or service and methodName are not specified");
        }

        char paramsSeparator = StringUtils.contains(targetURI, "?") ? '&' : '?';
        String paramsStr = queryParameters.isEmpty() ? "" : paramsSeparator + buildParamsStr().toString();

        String location = targetURI + paramsStr;

        return location;
    }

    /**
     * Performs HTTP redirect
     */
    public void redirect(Router router, HttpContext context) {
        String location = toLocation(router);
        HttpResponse response = context.getResponse();
        HttpUtils.setHeaders(response,headers);
        HttpUtils.setCookies(response,cookies);
        response.sendRedirect(location, statusCode);
    }

    /**
     * Performs router action forwarding
     */
    public void forward(Router router, HttpContext context) {
        String location = toLocation(router);
        ForwardRequest request = new ForwardRequest(context.getRequest(), location);
        context.setRequest(request);
        ActionResolution resolution = router.resolveAction(httpMethod, request.getRequestURI());
        router.performAction(resolution);
    }

    protected StringBuilder buildParamsStr() {
        StringBuilder paramsStrBuilder = new StringBuilder();
        boolean next = false;
        for (Map.Entry<String, String> e : queryParameters.entrySet()) {
            String paramNameEnc = URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8);
            String paramValEnc = URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8);
            if (next) {
                paramsStrBuilder.append("&");
            }
            paramsStrBuilder.append(paramNameEnc).append("=").append(paramValEnc);
            next = true;
        }
        return paramsStrBuilder;
    }

    public static class NavigationException extends RuntimeException {
        public NavigationException(String message) {
            super(message);
        }

        public NavigationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
