package colesico.framework.eventbus;

/**
 * Task executor config basis
 */
abstract public class AbstractEventExecutorConfig {

    /**
     * Initial task workers pool size
     */
    public int getCorePoolSize() {
        return 1;
    }

}
