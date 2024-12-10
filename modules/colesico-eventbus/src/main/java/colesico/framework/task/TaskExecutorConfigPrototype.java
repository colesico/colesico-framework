package colesico.framework.task;

/**
 * Task executor config basis
 */
abstract public class TaskExecutorConfigPrototype {

    public int awaitTerminationSeconds(){
        return 10;
    }

    /**
     * Initial task workers pool size
     */
    public int getCorePoolSize() {
        return 1;
    }

}
