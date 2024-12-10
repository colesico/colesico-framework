package colesico.framework.task;

/**
 * Process task synchronously
 */
public interface TaskDispatcher {

    <E> void dispatch(E task);

}
