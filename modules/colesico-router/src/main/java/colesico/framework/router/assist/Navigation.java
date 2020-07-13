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
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.router.Router;
import colesico.framework.router.RouterException;
import colesico.framework.service.ServiceProxy;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Navigation helper
 *
 * @author Vladlen Larionov
 */
public class Navigation<N extends Navigation> {

    protected String uri;
    protected Class<?> serviceClass;
    protected String methodName;
    protected HttpMethod httpMethod = HttpMethod.HTTP_METHOD_GET;
    protected int httpCode = 302;
    protected final Map<String, String> queryParameters = new HashMap<>();
    protected final Map<String, String> routeParameters = new HashMap<>();

    public Navigation() {
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
        this.methodName = methodName;
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

    public N httpCode(int httpCode) {
        this.httpCode = httpCode;
        return (N) this;
    }

    public String toLocation(Router router) {
        String targetURI;
        if (StringUtils.isNotBlank(this.uri)) {
            targetURI = uri;
        } else if (this.serviceClass != null && this.methodName != null) {
            List<String> slicedRoute = router.getSlicedRoute(this.serviceClass, this.methodName, this.httpMethod, this.routeParameters);
            if (slicedRoute == null) {
                throw new NavigationException("Unknown uri for service '" + serviceClass.getName() + "' and method name '" + methodName + "'");
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

    public static String buildURLPrefix(String requestScheme, String host, Integer port) {
        return requestScheme + "://" + host + (port == 80 ? "" : ":" + port);
    }

    public void redirect(Router router, HttpContext context) {
        String location = toLocation(router);
        context.getResponse().sendRedirect(location, httpCode);
    }


    protected StringBuilder buildParamsStr() {
        StringBuilder paramsStrBuilder = new StringBuilder();
        boolean next = false;
        for (Map.Entry<String, String> e : queryParameters.entrySet()) {
            String paramValEnc = URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8);
            if (next) {
                paramsStrBuilder.append("&");
            }
            paramsStrBuilder.append(e.getKey()).append("=").append(paramValEnc);
            next = true;
        }
        return paramsStrBuilder;
    }

    public static class NavigationException extends RuntimeException {
        public NavigationException(String message) {
            super(message);
        }
    }

}
