package colesico.framework.test.example.helloworld;

import colesico.framework.config.Config;
import colesico.framework.ioc.Rank;
import colesico.framework.undertow.UndertowConfigPrototype;
import io.undertow.Undertow;

@Config(rank = Rank.RANK_TEST)
public class UndertowTestConfig extends UndertowConfigPrototype {

    @Override
    public void applyOptions(Undertow.Builder builder) {
        builder.addHttpListener(8085, "localhost");
    }
}
