package colesico.framework.router;

import colesico.framework.teleapi.TeleController;
import colesico.framework.teleapi.TeleCommand;

import java.util.Map;

/**
 * Represents any action associated with given route
 */
public final class RouteAction {

    /**
     * Target controller
     */
    private final TeleController<?, Router.Invocation, RouterCommands> teleController;

    /**
     * Target method, invoked to handle the request.
     * This is the method of tele-facade for the appropriate service (weblet, restlet)
     */
    private final TeleCommand<?, ?> teleCommand;

    /**
     * Extra attributes bound to action method
     * Attributes can be null.
     *
     * @see RouteAttribute
     */
    private final Map<String, String> attributes;

    public RouteAction(TeleController<?, Router.Invocation, RouterCommands> teleController,
                       TeleCommand<?, ?> teleCommand,
                       Map<String, String> attributes) {

        this.teleController = teleController;
        this.teleCommand = teleCommand;
        this.attributes = attributes;
    }

    public TeleController<?, Router.Invocation, RouterCommands> teleController() {
        return teleController;
    }

    public TeleCommand<?, ?> teleCommand() {
        return teleCommand;
    }

    public Map<String, String> attributes() {
        return attributes;
    }
}
