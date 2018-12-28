package colesico.framework.eventbus;

public interface EventBus {

    <E> void send(E event);
}
