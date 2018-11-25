package colesico.examples.ioc.singleton;

import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

import javax.inject.Singleton;

@Producer
@Produce(MySingleton1.class)
public class MyProducer {

    @Singleton
    public MySingleton2 getSingleton2(){
        return new MySingleton2();
    }
}
