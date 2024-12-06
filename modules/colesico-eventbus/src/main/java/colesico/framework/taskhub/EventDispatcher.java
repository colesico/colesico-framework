package colesico.framework.taskhub;

/**
 * Immediate event execution
 */
public interface EventDispatcher {
    <E> void dispatch(E event);
}
