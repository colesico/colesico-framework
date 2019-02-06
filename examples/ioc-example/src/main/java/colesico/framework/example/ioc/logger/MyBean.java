package colesico.framework.example.ioc.logger;


import colesico.framework.ioc.Contextual;

public class MyBean {
    private final Logger logger;

    public MyBean(@Contextual Logger logger) {
        this.logger = logger;
    }

    public void run(){
        logger.log("Hello from MyBean");
    }
}
