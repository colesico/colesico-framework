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
package colesico.framework.httprouter.assist;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.httprouter.Router;
import colesico.framework.httprouter.RouterException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Navigation helper
 *
 * @author Vladlen Larionov
 */
public class Navigation {

    protected Action action = Action.REDIRECT;

    protected Class<?> serviceClass;
    protected String serviceMethod;

    protected HttpMethod httpMethod = HttpMethod.HTTP_METHOD_GET;
    protected String uri;

    protected int status = 302;

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

    public static Navigation of(Class<?> serviceClass, String serviceMethod) {
        return new Navigation().serviceClass(serviceClass).serviceMethod(serviceMethod);
    }

    public Navigation uri(String uri) {
        this.uri = uri;
        return this;
    }

    public Navigation serviceClass(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
        return this;
    }

    public Navigation serviceMethod(String methodName) {
        this.serviceMethod = methodName;
        return this;
    }

    public Navigation httpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public Navigation queryParam(String name, String value) {
        if (value != null) {
            queryParameters.put(name, value);
        }
        return this;
    }

    /**
     * Set query parameters
     */
    public Navigation queryParams(Map<String, String> params) {
        this.queryParameters.putAll(params);
        return this;
    }

    public Navigation routeParam(String name, String value) {
        routeParameters.put(name, value);
        return this;
    }

    /**
     * Set route parameters
     */
    public Navigation routeParams(Map<String, String> params) {
        this.routeParameters.putAll(params);
        return this;
    }

    /**
     * Set custom http redirect status.
     * Default code 302
     */
    public Navigation status(int code) {
        this.status = code;
        return this;
    }

    /**
     * Set HTTP header
     */
    public Navigation header(String name, String vale) {
        List<String> hValues = headers.computeIfAbsent(name, n -> new ArrayList<>());
        hValues.add(vale);
        return this;
    }

    public Navigation headers(Map<String, List<String>> headers) {
        for (var entry : headers.entrySet()) {
            this.headers.computeIfAbsent(entry.getKey(), k -> new ArrayList<>())
                    .addAll(entry.getValue());
        }
        return this;
    }

    /**
     * Set HTTP cookie
     */
    public Navigation cookie(HttpCookie cookie) {
        cookies.add(cookie);
        return this;
    }

    public Navigation cookies(Set<HttpCookie> cookies) {
        this.cookies.addAll(cookies);
        return this;
    }

    public Navigation action(Action action) {
        this.action = action;
        return this;
    }

    /**
     * Returns url to use in http redirect/forward
     */
    public String toLocation(Router router) {
        String targetURI;
        if (!StringUtils.isBlank(uri)) {
            targetURI = uri;
        } else if (this.serviceClass != null && this.serviceMethod != null) {
            List<String> slicedRoute = router.slicedRoute(this.serviceClass, this.serviceMethod, this.httpMethod, this.routeParameters);
            if (slicedRoute == null) {
                throw new NavigationException("Unknown uri for service '" + serviceClass.getName() + "' and method name '" + serviceMethod + "'");
            }
            slicedRoute.remove(0);
            targetURI = RouteTrie.SEGMENT_DELEMITER + String.join(RouteTrie.SEGMENT_DELEMITER, slicedRoute);
        } else {
            throw new NavigationException("Location URI or service and methodName are not specified");
        }

        char paramsSeparator = targetURI.contains("?") ? '&' : '?';
        String paramsStr = queryParameters.isEmpty() ? "" : paramsSeparator + buildParamsStr().toString();

        String location = targetURI + paramsStr;

        return location;
    }

    /**
     * Performs HTTP redirect
     */
    public void redirect(Router router, HttpContext context) {
        String location = toLocation(router);
        HttpResponse response = context.response();
        HttpUtils.setHeaders(response, headers);
        HttpUtils.setCookies(response, cookies);
        response.setStatus(status).addHeader("Location", location).close();
    }

    /**
     * Performs router action forwarding
     */
    public void forward(Router router, HttpContext context) {
        String location = toLocation(router);
        ForwardRequest request = new ForwardRequest(context.request(), location);
        context.setRequest(request);
        Optional<Router.Invocation> resolution = router.resolve(new Router.Criteria(httpMethod, request.path()));
        router.execute(resolution.get());
    }

    public void navigate(Router router, HttpContext context) {
        switch (action) {
            case REDIRECT -> redirect(router, context);
            case FORWARD -> forward(router, context);
        }
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

    public static class NavigationException extends RouterException {
        public NavigationException(String message) {
            super(message);
        }

        public NavigationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public enum Action {
        REDIRECT,
        FORWARD
    }

}
