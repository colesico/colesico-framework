package colesico.examples.ioc.plugin;

import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;


@Producer(Rank.RANK_EXTENSION)
@Produce(MyPlugin.class)
public class MyPluginProducer {

    public MyPluginInterface getMyPluginInterface(MyPlugin impl){
        return impl;
    }
}
