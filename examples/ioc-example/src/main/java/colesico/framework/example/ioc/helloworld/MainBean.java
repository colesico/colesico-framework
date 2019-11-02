package colesico.framework.example.ioc.helloworld;

public class MainBean {

    private final HelloBean helloService;

    public MainBean(HelloBean helloService) {
        this.helloService = helloService;
    }

    public String sayHello() {
        return helloService.sayHello();
    }
}
