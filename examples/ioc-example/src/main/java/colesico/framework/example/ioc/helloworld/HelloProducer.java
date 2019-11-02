package colesico.framework.example.ioc.helloworld;


import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

// Declare producer
@Producer
// Declare default producing: new HelloService(...)
@Produce(HelloBean.class)
// Declare default producing: new MainService(...)
@Produce(MainBean.class)
public class HelloProducer {
}
