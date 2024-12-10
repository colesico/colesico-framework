package colesico.framework.task;

import java.util.Collection;

/**
 * Process task synchronously
 */
public interface TaskDispatcher {

    <T, R> Collection<R> dispatch(T task);

}
