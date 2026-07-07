package colesico.framework.router;

import colesico.framework.teleapi.TeleController;
import colesico.framework.teleapi.TeleFacade;

import java.util.Optional;

/**
 * This interface must provide a controller that will call the router to execute the invocation.
 */
public interface TargetController<Q extends TeleController.Criteria> extends TeleController<Q, Router.Invocation, RouterCommandsRegistry> {

    /**
     * Returns http router target controller all tele-facades
     */
    Iterable<TeleFacade<?, RouterCommandsRegistry>> teleFacades();

    default Optional<Router.Invocation> resolve(Criteria criteria) {
        return Optional.empty();
    }

    default void register(TeleFacade<?, RouterCommandsRegistry> teleFacade) {
        throw new UnsupportedOperationException("Not supported");
    }
}

