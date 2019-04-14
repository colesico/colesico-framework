package colesico.framework.example.ioc.multiplugin;

import colesico.framework.ioc.Polyproduce;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;


@Producer(Rank.RANK_MINOR)
@Produce(MyPlugin1.class)
@Produce(MyPlugin2.class)
@Produce(MyHostBean.class)
public class MyProducer {

    // @Polyproduce indicates multiple MyPluginInterface implementations
    @Polyproduce
    public MyPluginInterface getMyPluginInterface1(MyPlugin1 impl) {
        return impl;
    }

    @Polyproduce
    public MyPluginInterface getMyPluginInterface2(MyPlugin2 impl) {
        return impl;
    }
}
