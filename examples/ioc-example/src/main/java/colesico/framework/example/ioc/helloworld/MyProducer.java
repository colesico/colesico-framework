package colesico.framework.example.ioc.helloworld;


import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

// Declare producer
@Producer
// Declare default producing: new MyService(...)
@Produce(MyService.class)
// Declare default producing: new MyServiceHolder(...)
@Produce(MyServiceHolder.class)
public class MyProducer {
}
