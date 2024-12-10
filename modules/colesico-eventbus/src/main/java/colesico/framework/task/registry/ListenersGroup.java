package colesico.framework.task.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

public final class ListenersGroup<E,R> {

    private final Collection<TaskHandler<E,R>> listeners = new ArrayList<>();

    public boolean isEmpty() {
        return listeners.isEmpty();
    }
    
    public void add(TaskHandler<E,R> listener) {
        listeners.add(listener);
    }

    /**
     * Apply action on each listener within the group
     */
    public void apply(Consumer<TaskHandler<E,R>> action) {
        if (!listeners.isEmpty()) {
            for (var listener : listeners) {
                action.accept(listener);
            }
        }
    }

    public Iterator<TaskHandler<E,R>> iterator() {
        return listeners.iterator();
    }

}
