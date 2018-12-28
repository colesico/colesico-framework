package colesico.framework.eventbus;

@FunctionalInterface
public interface EventHandler<E> {
    void onEvent(E event);
}
