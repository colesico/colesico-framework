package colesico.framework.example.ioc.implement;


import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

// Declare producer
@Producer
// Declare default producing
@Produce(BeanImpl.class)
public class ImplProducer {

    // Declare custom producing for BeanInterface
    public BeanInterface getBean(BeanImpl impl){
        return impl;
    }
}
