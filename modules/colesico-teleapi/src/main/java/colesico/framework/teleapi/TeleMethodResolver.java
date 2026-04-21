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
        F extends TeleFacade<?, D, R, W>,
        D extends MethodDescriptor,
        R extends TRContext,
        W extends TWContext> {

    protected final Logger log = LoggerFactory.getLogger(TeleMethodResolver.class);

    abstract protected void addTeleFacade(F teleFacade);

    abstract protected void addMethodDescriptor(D descriptor);

    /**
     * Resolve target method based on exchange data
     */
    abstract public TeleMethod resolve(P protocolContext) throws DescriptorNotFoundException;

    public void lookupTeleFacades(Polysupplier<F> teleFacadeSupp) {
        log.debug("Lookup tele-facades...");
        for (F teleFacade : teleFacadeSupp) {
            log.debug("Found tele-facade: {}", teleFacade.getClass().getName());
            addTeleFacade(teleFacade);
            for (D descriptor : teleFacade.descriptors()) {
                addMethodDescriptor(descriptor);
                log.debug(" > descriptor: {}", descriptor);
            }
        }
    }

}
