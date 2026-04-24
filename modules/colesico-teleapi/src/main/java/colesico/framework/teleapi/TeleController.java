package colesico.framework.teleapi;

import java.util.Optional;

/**
 * Controls tele-methods invocation process considering invocation environment (protocol etc.),
 * performs error handling, etc.
 *
 * @param <C> Context to resolve an invocation (e.g. HTTP URL, method name, etc.)
 * @param <I> Invocation to perform
 * @param <F> Common (abstract) tele-facade class
 */
public interface TeleController<C, I, F extends TeleFacade<?, ?>> {

    /**
     * Resolve invocation with protocol context.
     *
     * @return invocation to be performed or empty result.
     */
    Optional<I> resolve(C context);

    /**
     * Perform invocation.
     * Before call target method creates data-port, assign it to current thread then invoke tele method.
     * Also performs error handling.
     */
    void perform(I invocation);

    /**
     * Register tele-facade
     */
    void register(F teleFacade);

}
