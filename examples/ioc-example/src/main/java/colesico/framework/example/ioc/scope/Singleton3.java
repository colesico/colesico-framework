package colesico.framework.example.ioc.scope;

public class Singleton3 {
    private int counter = 0;

    public String getMessage() {
        return "Singleton3-" + (counter++);
    }
}
