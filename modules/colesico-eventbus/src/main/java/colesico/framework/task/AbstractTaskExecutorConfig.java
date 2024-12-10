package colesico.framework.task;

/**
 * Task executor config basis
 */
abstract public class AbstractTaskExecutorConfig {

    public int awaitTerminationSeconds() {
        return 10;
    }

}
