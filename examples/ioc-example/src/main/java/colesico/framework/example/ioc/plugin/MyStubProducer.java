package colesico.framework.example.ioc.plugin;

import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;


@Producer(Rank.RANK_MINOR)
@Produce(MyStubBean.class)
public class MyStubProducer {
    public MyPluginInterface getMyPluginInterface(MyStubBean impl) {
        return impl;
    }
}
