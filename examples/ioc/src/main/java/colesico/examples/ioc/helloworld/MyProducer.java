package colesico.examples.ioc.helloworld;


import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

@Producer
@Produce(MyService.class)
@Produce(MyServiceHolder.class)
public class MyProducer {
}
