package colesico.framework.asyncjob;


public interface JobService extends JobEnqueuer {
    void start();

    void stop();

    boolean isRunning();

}
