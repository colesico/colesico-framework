package colesico.framework.task;

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * Process task synchronously
 */
public interface TaskDispatcher {

    <T, R> Collection<R> dispatch(T task);

}
