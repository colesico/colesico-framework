package colesico.framework.example.ioc.multiplugin;

import colesico.framework.ioc.Polyproduce;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;


@Producer(Rank.RANK_MINOR)
@Produce(Plugin1.class)
@Produce(Plugin2.class)
@Produce(MainBean.class)
public class MultipluginProducer {

    // @Polyproduce indicates multiple MyPluginInterface implementations
    @Polyproduce
    public PluginInterface getMyPluginInterface1(Plugin1 impl) {
        return impl;
    }

    @Polyproduce
    public PluginInterface getMyPluginInterface2(Plugin2 impl) {
        return impl;
    }
}
