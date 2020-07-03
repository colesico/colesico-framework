package colesico.framework.asynctask;

/**
 * Task executor config basis
 */
abstract public class TaskExecutorConfigBase {

    abstract public Class<?> getPayloadType();

    public TaskConsumer getTaskConsumer() {
        return null;
    }

    public int getCorePoolSize() {
        return 1;
    }

    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + "{ payload=" + getPayloadType() + '}';
    }

}
