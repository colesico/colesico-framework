package colesico.framework.example.ioc.scope;

public class RefreshScoped1 {
    private int counter = 0;

    public String getMessage() {
        return "RefreshScoped1-" + (counter++);
    }
}
