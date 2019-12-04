package colesico.framework.example.config.classed;

import colesico.framework.config.Config;
import colesico.framework.ioc.Classed;

@Config
@Classed(Classifier.class)
public class SingleConfig extends SingleConfigPrototype {
    @Override
    public String getValue() {
        return "SingleConfig";
    }
}
