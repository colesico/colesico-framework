package colesico.framework.example.asynctask;

/**
 * Task data
 */
public class TaskPayload {

    public String value;

    public TaskPayload(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
