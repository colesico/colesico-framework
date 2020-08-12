package colesico.framework.router;

import colesico.framework.teleapi.TeleHandler;

import java.util.Map;

/**
 * Route action
 */
public final class RouteAction {

    /**
     * Action method, invoked to handle the request.
     * This is the method of tele-facade for the appropriate service (weblet, restlet)
     */
    private final TeleHandler teleMethod;

    /**
     * Extra attributes bound to action method
     * Attributes can be null.
     *
     * @see RouteAttribute
     */
    private final Map<String, String> attributes;

    public RouteAction(TeleHandler teleMethod, Map<String, String> attributes) {
        this.teleMethod = teleMethod;
        this.attributes = attributes;
    }

    public TeleHandler getTeleMethod() {
        return teleMethod;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
