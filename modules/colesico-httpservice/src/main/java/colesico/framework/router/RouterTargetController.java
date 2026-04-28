package colesico.framework.router;

import colesico.framework.teleapi.TeleController;
import colesico.framework.teleapi.TeleFacade;

/**
 * This interface must provide a controller that will call the router to execute the invocation.
 */
public interface RouterTargetController<Q> extends TeleController<Q, Router.Invocation, RouterCommands> {

    /**
     *  Target controllers all tele-facades
     */
    Iterable<TeleFacade<?, RouterCommands>> teleFacades();
}

