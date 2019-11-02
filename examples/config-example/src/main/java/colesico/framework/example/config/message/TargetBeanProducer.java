package colesico.framework.example.config.message;


import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

@Producer
@Produce(value = TargetBean.class, classed = MessageConfigPrototype.class)
public class TargetBeanProducer {
}
