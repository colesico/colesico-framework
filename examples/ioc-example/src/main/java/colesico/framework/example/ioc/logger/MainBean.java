package colesico.framework.example.ioc.logger;


import colesico.framework.ioc.Contextual;

public class MainBean {

    private final Logger logger;

    public MainBean(@Contextual Logger logger) {
        this.logger = logger;
    }

    public String getLogMessage() {
        return logger.log("Message");
    }
}
