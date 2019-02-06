package colesico.framework.example.ioc.helloworld;

public class MyServiceHolder {

    private final MyService myService;

    public MyServiceHolder(MyService myService) {
        this.myService = myService;
    }

    public void run() {
        myService.printHello();
    }
}
