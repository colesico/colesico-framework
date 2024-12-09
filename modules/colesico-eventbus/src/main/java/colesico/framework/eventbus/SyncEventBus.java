package colesico.framework.eventbus;

/**
 * Process event synchronously
 */
public interface SyncEventBus {

    <E> void dispatch(E event);

}
