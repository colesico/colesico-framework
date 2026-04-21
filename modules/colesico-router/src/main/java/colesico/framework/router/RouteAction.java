package colesico.framework.router;

import colesico.framework.teleapi.MethodDescriptor;

import java.util.Map;

/**
 * Represents any action associated with given route
 */
public final class RouteAction {

    /**
     * Action method, invoked to handle the request.
     * This is the method of tele-facade for the appropriate service (weblet, restlet)
     */
    private final MethodDescriptor teleMethod;

    /**
     * Extra attributes bound to action method
     * Attributes can be null.
     *
     * @see RouteAttribute
     */
    private final Map<String, String> attributes;

    public RouteAction(MethodDescriptor teleMethod, Map<String, String> attributes) {
        this.teleMethod = teleMethod;
        this.attributes = attributes;
    }

    public MethodDescriptor getTeleMethod() {
        return teleMethod;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
