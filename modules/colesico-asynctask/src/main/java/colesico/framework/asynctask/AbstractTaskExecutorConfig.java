package colesico.framework.asynctask;

/**
 * Task executor config basis
 */
abstract public class AbstractTaskExecutorConfig {

    protected Integer awaitTerminationSeconds = 5;

    public Integer getAwaitTerminationSeconds() {
        return awaitTerminationSeconds;
    }

    public void setAwaitTerminationSeconds(Integer awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
    }
}
