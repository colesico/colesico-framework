package colesico.framework.example.ioc.message;

import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

@Producer
@Produce(MainBeanMSG.class)
@Produce(ReceiverBean.class)
public class MessageProducer {
}
