package colesico.framework.teleapi;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.teleapi.dataport.TRContext;
import colesico.framework.teleapi.dataport.TWContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tele-method resolver.
 * Determines the method to be called.
 *
 * @param <P> - Protocol context API  (request + response)
 */
abstract public class TeleMethodResolver<P,
        F extends TeleFacade<?, M, R, W>,
        M extends TeleMethodReference,
        R extends TRContext,
        W extends TWContext> {

    protected final Logger log = LoggerFactory.getLogger(TeleMethodResolver.class);

    abstract protected void addTeleFacade(F teleFacade);

    abstract protected void addTeleMethod(M teleMethodRef);

    /**
     * Resolve target method based on exchange data
     */
    abstract public TeleMethod resolve(P protocolContext) throws TeleMethodNotFoundException;

    public void lookupTeleFacades(Polysupplier<F> teleFacadeSupp) {
        log.debug("Lookup tele-facades...");
        for (F teleFacade : teleFacadeSupp) {
            log.debug("Found tele-facade: {}", teleFacade.getClass().getName());
            addTeleFacade(teleFacade);
            for (M teleMethodRef : teleFacade.methods()) {
                addTeleMethod(teleMethodRef);
                log.debug(" > tele-method: {}", teleMethodRef);
            }
        }
    }

}
