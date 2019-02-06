package colesico.framework.example.ioc.named;

import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

import javax.inject.Named;

@Producer
@Produce(value = MyBean.class,named = "default")
@Produce(MyBeanHolder.class)
public class MyProducer {

    @Named("custom")
    public MyBean getMyNamed(){
        return new MyBean("Custom");
    }
}
