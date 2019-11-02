package colesico.framework.example.ioc.logger;

public class Logger {

    private final String loggerName;

    public Logger(String loggerName) {
        this.loggerName = loggerName;
    }

    public String log(String message) {
        return loggerName + ":" + message;
    }
}
