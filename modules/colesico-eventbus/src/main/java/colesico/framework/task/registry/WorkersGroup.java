package colesico.framework.task.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 * Task workers bind to task class
 */
public final class WorkersGroup<T, R> {

    private final Collection<TaskWorker<T, R>> workers = new ArrayList<>();

    public boolean isEmpty() {
        return workers.isEmpty();
    }

    public void add(TaskWorker<T, R> worker) {
        workers.add(worker);
    }

    public Iterator<TaskWorker<T, R>> iterator() {
        return workers.iterator();
    }

    public <O> Collection<O> apply(Function<TaskWorker<T, R>, O> action) {
        Collection<O> result = new ArrayList<>();
        for (var worker : workers) {
            result.add(action.apply(worker));
        }
        return result;
    }
}
