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
package colesico.framework.router.assist;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.router.Router;
import colesico.framework.service.ServicePrototype;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Navigation helper
 *
 * @author Vladlen Larionov
 */
public class Navigation<N extends Navigation>{

    protected String uri;
    protected Class<?> framletClass;
    protected String methodName;
    protected HttpMethod httpMethod = HttpMethod.GET;
    protected int httpCode = 302;
    protected final Map<String, String> parameters = new HashMap<>();
    protected final Map<String, String> uriParameters = new HashMap<>();

    public Navigation() {
    }

    public Navigation(Class<?> framletClass, String methodName) {
        this.framletClass = framletClass;
        this.methodName = methodName;
    }

    public Navigation(String uri) {
        this.uri = uri;
    }

    public N uri(String uri) {
        this.uri = uri;
        return (N) this;
    }

    public N framlet(Class<?> framletClass) {
        this.framletClass = framletClass;
        return (N)this;
    }

    public N framlet(Object framletInstance) {
        this.framletClass = ((ServicePrototype) framletInstance).getSuperClass();
        return (N)this;
    }

    public N method(String methodName) {
        this.methodName = methodName;
        return (N)this;
    }

    public N httpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return (N)this;
    }

    public N param(String name, String value) {
        parameters.put(name, value);
        return (N)this;
    }

    public N uriParam(String name, String value) {
        uriParameters.put(name, value);
        return (N)this;
    }

    public N httpCode(int httpCode) {
        this.httpCode = httpCode;
        return (N)this;
    }

    public String toLocation(Router router, HttpContext context) {
        String targetURI;
        if (StringUtils.isNotBlank(this.uri)) {
            targetURI = uri;
        } else if (this.framletClass != null && this.methodName != null) {
            List<String> slicedRoute = router.getSlicedRoute(this.framletClass, this.methodName, this.httpMethod, this.uriParameters);
            if (slicedRoute == null) {
                throw new NavigationException("Unknown uri for framelet '" + framletClass.getName() + "' and method name '" + methodName+"'");
            }
            slicedRoute.remove(0);
            targetURI = RouteTrie.SEGMENT_DELEMITER + StringUtils.join(slicedRoute, RouteTrie.SEGMENT_DELEMITER);
        } else {
            throw new NavigationException("Location URI or service and methodName are not specified");
        }

        String urlPrefix;
        if (context != null) {
            HttpRequest request = context.getRequest();
            urlPrefix = buildURLPrefix(request.getRequestScheme(), request.getHost(), request.getPort());
        } else {
            urlPrefix = "";
        }

        char paramsSeparator = StringUtils.contains(targetURI, "?") ? '&' : '?';
        String paramsStr = parameters.isEmpty() ? "" : paramsSeparator + buildParamsStr().toString();

        String location = urlPrefix + targetURI + paramsStr;

        return location;
    }

    public static String buildURLPrefix(String requestScheme, String host, Integer port) {
        return requestScheme + "://" + host + (port == 80 ? "" : ":" + port);
    }

    public void redirect(Router router, HttpContext context){
        String location = toLocation(router,context);
        context.getResponse().sendRedirect(location,httpCode);
    }


    protected StringBuilder buildParamsStr() {
        StringBuilder paramsStrBuilder = new StringBuilder();
        try {
            boolean next = false;
            for (Map.Entry<String, String> e : parameters.entrySet()) {
                String paramValEnc = URLEncoder.encode(e.getValue(), "UTF-8");
                if (next) {
                    paramsStrBuilder.append("&");
                }
                paramsStrBuilder.append(e.getKey()).append("=").append(paramValEnc);
                next = true;
            }
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return paramsStrBuilder;
    }

    public static class NavigationException extends RuntimeException{
        public NavigationException(String message) {
            super(message);
        }
    }

}
