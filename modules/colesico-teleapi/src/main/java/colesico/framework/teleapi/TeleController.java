package colesico.framework.teleapi;

import java.util.Optional;

/**
 * Controls tele-methods invocation process considering invocation environment (protocol etc.),
 * performs error handling, etc.
 *
 * @param <P> Protocol level context (e.g. HttpContext)
 * @param <M> Tele-method resolution
 * @param <F> Common (abstract) tele-facade class
 */
public interface TeleController<P, M extends TeleResolution<?, ?>, F extends TeleFacade<?, ?>> {

    /**
     * Resolve tele-method with protocol context.
     *
     * @return tele-method resolution or empty result.
     */
    Optional<M> resolve(P protocolContext);

    /**
     * Invoke resolved tele-method.
     * Before invocation creates data-port, assign it to current thread then invoke tele method.
     * Also performs error handling.
     */
    void invoke(M teleMethod);

    /**
     * Register tele-facade
     */
    void register(F teleFacade);

}
