package colesico.framework.eventbus.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

public final class ListenersGroup<E> {

    private final Collection<EventListener<E>> listeners = new ArrayList<>();

    public boolean isEmpty() {
        return listeners.isEmpty();
    }
    
    public void add(EventListener<E> listener) {
        listeners.add(listener);
    }

    /**
     * Apply action on each listener within the group
     */
    public void apply(Consumer<EventListener<E>> action) {
        if (!listeners.isEmpty()) {
            for (var listener : listeners) {
                action.accept(listener);
            }
        }
    }

    public Iterator<EventListener<E>> iterator() {
        return listeners.iterator();
    }

}
