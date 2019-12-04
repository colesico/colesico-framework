package colesico.framework.example.config.classed;

import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Polysupplier;

public class ClassedConfigsBean {
    private final SingleConfigPrototype singleConfig;
    private final Polysupplier<PolyConfigPrototype> polyConfigSup;

    public ClassedConfigsBean(
        @Classed(Classifier.class)
        SingleConfigPrototype singleConfig,
        @Classed(Classifier.class)
        Polysupplier<PolyConfigPrototype> polyConfigSup) {
        this.singleConfig = singleConfig;
        this.polyConfigSup = polyConfigSup;
    }

    public String getValues(){
        return singleConfig.getValue()+";"+polyConfigSup.iterator(null).next().getValue();
    }
}
