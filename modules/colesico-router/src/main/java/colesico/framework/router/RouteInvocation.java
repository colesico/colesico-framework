package colesico.framework.router;

import colesico.framework.http.HttpMethod;
import colesico.framework.teleapi.TeleController;

import java.util.Map;

public final class RouteInvocation {

    /**
     * Target controller
     */
    private final TeleController<?, RouteInvocation, ?> controller;

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

    public RouteInvocation(TeleController<?, RouteInvocation, ?> controller,
                           HttpMethod requestMethod,
                           String requestUri,
                           RouteAction routeAction,
                           Map<String, String> routeParameters) {

        this.controller = controller;
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

    public TeleController<?, RouteInvocation, ?> getController() {
        return controller;
    }
}
