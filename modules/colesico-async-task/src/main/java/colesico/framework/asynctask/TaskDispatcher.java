package colesico.framework.asynctask;

import java.util.Collection;

/**
 * Process task synchronously
 */
public interface TaskDispatcher {

    <T> void dispatch(T task);

    <T, R> Collection<R> dispatchReturn(T task);

}
