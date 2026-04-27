package colesico.framework.router;

import colesico.framework.teleapi.TeleController;
import colesico.framework.teleapi.TeleFacade;

public interface RouterTargetController<Q> extends TeleController<Q, Router.Invocation, RouterCommands> {
    Iterable<TeleFacade<?, RouterCommands>> teleFacades();
}

