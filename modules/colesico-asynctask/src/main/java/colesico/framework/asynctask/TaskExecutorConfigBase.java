package colesico.framework.asynctask;

/**
 * Task executor config basis
 */
abstract public class TaskExecutorConfigBase {

    /**
     * Task payload type. This type will be associated with concrete task executor
     */
    abstract public Class<?> getPayloadType();

    /**
     * Task performer for the given payload type.
     * If null the event bus will acts as a performer
     */
    public TaskPerformer getTaskConsumer() {
        return null;
    }

    /**
     * Initial task workers pool size
     */
    public int getCorePoolSize() {
        return 1;
    }

    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + "{ payload=" + getPayloadType() + '}';
    }

}
