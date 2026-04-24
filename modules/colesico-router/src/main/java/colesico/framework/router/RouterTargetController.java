package colesico.framework.router;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.teleapi.TeleController;
import colesico.framework.teleapi.TeleFacade;

public interface RouterTargetController<F extends TeleFacade<?, RouterDescriptors>> extends TeleController<Router.ResolveContext, RouterInvocation, F> {
    Polysupplier<F> teleFacades();
}

