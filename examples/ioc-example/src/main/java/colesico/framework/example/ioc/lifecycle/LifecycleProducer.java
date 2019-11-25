package colesico.framework.example.ioc.lifecycle;

import colesico.framework.ioc.Message;
import colesico.framework.ioc.PostProduce;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

@Producer
@Produce(value = MainBeanLFC.class, postProduce = true)
public class LifecycleProducer {

    // This is post produce listener
    @PostProduce
    public MainBeanLFC postProduce(@Message MainBeanLFC instance) {
        instance.setValue("Value");
        return instance;
    }
}
