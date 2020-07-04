package colesico.framework.example.asynctask.performer;

/**
 * Task data
 */
public class PerfTaskPayload {

    public String value;

    public PerfTaskPayload(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
