package colesico.framework.example.ioc.logger;


import colesico.framework.ioc.InjectionPoint;
import colesico.framework.ioc.Message;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

@Producer
@Produce(MainBean.class)
public class LoggerProducer {

    public Logger getLogger(@Message InjectionPoint ip) {
        if (ip == null) {
            return new Logger("NonameLogger");
        }
        return new Logger(ip.getTargetClass().getName() + "Logger");
    }
}
