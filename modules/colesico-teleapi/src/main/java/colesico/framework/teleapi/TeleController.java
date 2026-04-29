package colesico.framework.teleapi;

import java.util.Optional;

/**
 * Controls tele-methods invocation process considering invocation environment (protocol etc.),
 * performs error handling, etc.
 *
 * @param <Q> Criteria to resolve an invocation (e.g. HTTP URL, method name, etc.)
 * @param <I> Invocation to execute
 * @param <M> Commands registry of tele-facade (references to target methods)
 */
public interface TeleController<Q extends TeleCriteria, I extends TeleInvocation, M extends TeleCommands> {

    /**
     * Resolve invocation with protocol context.
     *
     * @return invocation to be executed or empty result.
     */
    Optional<I> resolve(Q criteria);

    /**
     * Execute an invocation.
     * Before call target method creates data-port, assign it to current thread then invoke tele-command.
     * Also performs error handling.
     */
    void execute(I invocation);

    /**
     * Register tele-facade
     */
    void register(TeleFacade<?, M> teleFacade);

    default void register(Iterable<TeleFacade<?, M>> teleFacades) {
        for (var teleFacade : teleFacades) {
            register(teleFacade);
        }
    }
}
