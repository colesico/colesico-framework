package colesico.framework.teleapi;

import java.util.Optional;

/**
 * Controls tele-methods invocation process considering invocation environment (protocol etc.),
 * performs error handling, etc.
 *
 * @param <C> Criteria to resolve an invocation (e.g. HTTP URL, method name, etc.)
 * @param <I> Invocation to perform
 * @param <D> Descriptors registry of tele-facade (references to target methods)
 */
public interface TeleController<C, I, D> {

    /**
     * Resolve invocation with protocol context.
     *
     * @return invocation to be performed or empty result.
     */
    Optional<I> resolve(C criteria);

    /**
     * Perform invocation.
     * Before call target method creates data-port, assign it to current thread then invoke tele method.
     * Also performs error handling.
     */
    void perform(I invocation);

    /**
     * Register tele-facade
     */
    void register(TeleFacade<?, D> teleFacade);

    default void register(Iterable<TeleFacade<?, D>> teleFacades) {
        for (var teleFacade : teleFacades) {
            register(teleFacade);
        }
    }
}
