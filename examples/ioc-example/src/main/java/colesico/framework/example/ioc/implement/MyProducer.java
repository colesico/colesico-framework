package colesico.framework.example.ioc.implement;


import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

// Declare producer
@Producer
// Declare default producing
@Produce(MyImplementation.class)
public class MyProducer {

    // Declare custom producing for MyInterface
    public MyInterface getMyService(MyImplementation impl){
        return impl;
    }
}
