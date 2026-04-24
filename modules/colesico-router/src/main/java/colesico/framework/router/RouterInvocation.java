package colesico.framework.router;

import colesico.framework.http.HttpMethod;

import java.util.Map;

public final class RouterInvocation {

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
    private final RouteAction action;

    /**
     * Route parameters
     */
    private final Map<String, String> parameters;

    public RouterInvocation(HttpMethod requestMethod, String requestUri, RouteAction action, Map<String, String> parameters) {
        this.requestMethod = requestMethod;
        this.requestUri = requestUri;
        this.action = action;
        this.parameters = parameters;
    }

    public HttpMethod requestMethod() {
        return requestMethod;
    }

    public String requestUri() {
        return requestUri;
    }

    public RouteAction action() {
        return action;
    }

    public Map<String, String> parameters() {
        return parameters;
    }

}
