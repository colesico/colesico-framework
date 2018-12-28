package colesico.framework.eventbus;

public final class EventBinding<E> {

    private final Class<E> eventClass;
    private final EventHandler<E> handler;

    public EventBinding(Class<E> eventClass, EventHandler<E> handler) {
        this.eventClass = eventClass;
        this.handler = handler;
    }

    public Class<E> getEventClass() {
        return eventClass;
    }

    public EventHandler<E> getHandler() {
        return handler;
    }
}
