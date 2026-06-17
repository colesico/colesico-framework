package colesico.framework.ioc.scope;


import java.util.concurrent.ConcurrentHashMap;

/**
 * A task scope that uses {@link ScopedValue} to bind a mutable map of objects
 * to the current thread (or virtual thread). The scope is active only within
 * a {@link #forTask(Runnable)} or {@link #run(Callable)} block.
 * The data is automatically cleaned up after the block exits, making it suitable
 * for request scoped or task scoped dependencies in an IoC container.
 * <p>
 * This class implements {@link Scope} and delegates all operations to the map
 * bound to the current execution scope. If no scope is active, methods throw
 * {@link IllegalStateException}.
 * <p>
 * <strong>Note on mutability:</strong> This scope stores a mutable
 * {@link ConcurrentHashMap} inside the {@code ScopedValue}. This is required
 * by the {@link Scope} interface which expects {@code put}, {@code remove}
 * and {@code get(fabricator)} to mutate the scope. The mutability is safe
 * as long as the scope is accessed only from the current thread (or virtual thread)
 * and the map itself is thread-safe. Clients should be aware that any code
 * inside the scope can modify the map.
 * <p>
 * Typical usage (with an external IoC container creating a singleton instance):
 * <pre>{@code
 * TaskScope scope = container.getInstance(TaskScope.class);
 * scope.run(() -> {
 *     scope.put(SomeKey, someService);
 *     // ...
 * });
 * }</pre>
 *
 * @author Vladlen Larionov
 */

public interface TaskScope extends Scope {

    /**
     * Executes a {@link Runnable} task inside a new task scope bound to this {@code TaskScope} instance.
     * A fresh empty map is bound to the current thread for the duration of the task
     * and automatically discarded afterwards.
     *
     * @param task the task to run
     * @throws IllegalStateException if a scope is already active for this thread
     *                               that was created by the same {@code TaskScope} instance
     *                               (nested scopes are not allowed)
     */
    void forTask(Runnable task);
}
