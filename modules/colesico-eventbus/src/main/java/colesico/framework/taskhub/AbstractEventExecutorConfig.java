package colesico.framework.taskhub;

/**
 * Task executor config basis
 */
abstract public class AbstractEventExecutorConfig {

    /**
     * Task payload type. This type will be associated with concrete task executor
     */
    abstract public Class<?> getPayloadType();

    /**
     * Initial task workers pool size
     */
    public int getCorePoolSize() {
        return 1;
    }

}
