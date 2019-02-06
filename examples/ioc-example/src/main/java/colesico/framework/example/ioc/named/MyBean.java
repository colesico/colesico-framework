package colesico.framework.example.ioc.named;

import javax.inject.Inject;

public class MyBean {

    private final String name;

    @Inject
    public MyBean() {
        this.name = "Default";
    }

    public MyBean(String name) {
        this.name = name;
    }

    public void printHello(){
        System.out.println("Hello from "+name);
    }
}
