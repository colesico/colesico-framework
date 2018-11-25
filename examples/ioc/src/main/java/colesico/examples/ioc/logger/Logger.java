package colesico.examples.ioc.logger;

public class Logger {

    private final String name;

    public Logger(String name) {
        this.name = name;
    }

    public void log(String message){
        System.out.println(name+": "+message);
    }
}
