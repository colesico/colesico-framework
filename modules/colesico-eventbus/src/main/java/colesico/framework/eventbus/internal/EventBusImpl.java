package colesico.framework.eventbus.internal;

import colesico.framework.eventbus.EventBinding;
import colesico.framework.eventbus.EventBus;
import colesico.framework.eventbus.EventHandler;
import colesico.framework.eventbus.EventsListener;
import colesico.framework.ioc.InlineInject;
import colesico.framework.ioc.Polysupplier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class EventBusImpl implements EventBus {

    protected final Map<Class<?>, List<EventHandler<?>>> eventsBindings = new HashMap();

    @Inject
    public EventBusImpl(@InlineInject Polysupplier<EventsListener> listenersSupp) {
        initBindings(listenersSupp);
    }

    protected final void initBindings(Polysupplier<EventsListener> listenersSupp) {
        listenersSupp.forEach(
                listener -> {
                    EventBinding[] bindings = listener.getBindings();
                    for (EventBinding binding : bindings) {
                        List<EventHandler<?>> handlers = eventsBindings.computeIfAbsent(binding.getEventClass(), c -> new ArrayList<>());
                        handlers.add(binding.getHandler());
                    }
                }, null
        );
    }

    @Override
    public <E> void send(E event) {
        List<EventHandler<?>> handlers = eventsBindings.get(event.getClass());
        if (handlers != null) {
            for (EventHandler handler : handlers) {
                handler.onEvent(event);
            }
        }
    }
}
