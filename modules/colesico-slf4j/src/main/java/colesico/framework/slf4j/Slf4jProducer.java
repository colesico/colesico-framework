package colesico.framework.slf4j;

import colesico.framework.ioc.InjectionPoint;
import colesico.framework.ioc.Message;
import colesico.framework.ioc.Producer;
import colesico.framework.service.ServiceOrigin;
import colesico.framework.service.ServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Producer
public class Slf4jProducer {

    public Logger getLogger(@Message InjectionPoint ip) {
        if (ip==null){
            throw new RuntimeException("Undetermined target class for Logger injection");
        }
        if (ServiceProxy.class.isAssignableFrom(ip.getTargetClass())) {
            ServiceOrigin so = ip.getTargetClass().getAnnotation(ServiceOrigin.class);
            return LoggerFactory.getLogger(so.value());
        }
        return LoggerFactory.getLogger(ip.getTargetClass());
    }

}
