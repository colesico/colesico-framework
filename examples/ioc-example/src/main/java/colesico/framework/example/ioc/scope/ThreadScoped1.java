package colesico.framework.example.ioc.scope;

public class ThreadScoped1 {
    private int counter = 0;

    public String getMessage() {
        return "ThreadScoped1-" + (counter++);
    }
}
