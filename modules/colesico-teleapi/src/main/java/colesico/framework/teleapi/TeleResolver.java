package colesico.framework.teleapi;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.teleapi.dataport.TRContext;
import colesico.framework.teleapi.dataport.TWContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Determines the method to be called.
 *
 * @param <P> Protocol context API  (request + response)
 */
abstract public class TeleResolver<P,
        F extends TeleFacade<?, ?>,
        M extends TeleMethod<?, ?>> {

    protected Logger log = LoggerFactory.getLogger(TeleResolver.class);

    /**
     * Resolve tele-method with protocol context
     */
    abstract public M resolve(P protocolContext) throws MethodNotFoundException;

    /**
     * Register tele-facade in the resolver
     */
    abstract public void registerTeleFacade(F teleFacade);

    public void registerTeleFacades(Polysupplier<F> teleFacades) {
        log.debug("Tele-facades registration...");
        for (F teleFacade : teleFacades) {
            log.debug("Register tele-facade: {}", teleFacade.getClass().getName());
            registerTeleFacade(teleFacade);
        }
    }
}
