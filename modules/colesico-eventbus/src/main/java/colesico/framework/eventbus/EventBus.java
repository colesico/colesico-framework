package colesico.framework.eventbus;

/**
 * API for sending messages.
 */
public interface EventBus {
    /**
     * Send event
     * @param event
     * @param <E>
     */
    <E> void send(E event);
}
