package colesico.framework.router;

import colesico.framework.teleapi.TeleController;
import colesico.framework.teleapi.TeleFacade;

public interface RouterTargetController extends TeleController<Router.Criteria, Router.Invocation, RouterCommands> {
    Iterable<TeleFacade<?, RouterCommands>> teleFacades();
}

