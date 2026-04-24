package colesico.framework.router;

import colesico.framework.teleapi.TeleController;
import colesico.framework.teleapi.TeleFacade;

public interface RouterTargetController extends TeleController<Router.Criteria, Router.Invocation, RouterDescriptors> {
    Iterable<TeleFacade<?, RouterDescriptors>> teleFacades();
}

