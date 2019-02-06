package colesico.framework.example.ioc.logger;


import colesico.framework.ioc.InjectionPoint;
import colesico.framework.ioc.Message;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

@Producer
@Produce(MyBean.class)
@Produce(Main.class)
public class MyProducer {

    public Logger getLogger(@Message InjectionPoint ip) {
        if (ip==null){
            return new Logger("Noname logger");
        }
        return new Logger(ip.getTargetClass().getName());
    }
}
