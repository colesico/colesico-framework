package colesico.examples.ioc.implement;


import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

@Producer
@Produce(MyImplementation.class)
public class MyProducer {

    public MyInterface getMyService(MyImplementation impl){
        return impl;
    }
}
