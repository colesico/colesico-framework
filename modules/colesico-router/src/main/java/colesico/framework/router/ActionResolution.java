package colesico.framework.router;

import colesico.framework.http.HttpMethod;

import java.util.Map;

public final class ActionResolution {

    /**
     * Request http method
     */
    private final HttpMethod requestMethod;

    /**
     * Request URI
     */
    private final String requestUri;

    /**
     * Route Action
     */
    private final RouteAction routeAction;

    /**
     * Route parameters
     */
    private final Map<String, String> routeParameters;

    public ActionResolution(HttpMethod requestMethod, String requestUri, RouteAction routeAction, Map<String, String> routeParameters) {
        this.requestMethod = requestMethod;
        this.requestUri = requestUri;
        this.routeAction = routeAction;
        this.routeParameters = routeParameters;
    }

    public HttpMethod getRequestMethod() {
        return requestMethod;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public RouteAction getRouteAction() {
        return routeAction;
    }

    public Map<String, String> getRouteParameters() {
        return routeParameters;
    }
}
