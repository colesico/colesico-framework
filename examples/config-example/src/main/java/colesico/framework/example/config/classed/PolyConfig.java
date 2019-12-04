package colesico.framework.example.config.classed;

import colesico.framework.config.Config;
import colesico.framework.ioc.Classed;

@Config
@Classed(Classifier.class)
public class PolyConfig extends PolyConfigPrototype {
    @Override
    public String getValue() {
        return "PolyConfig";
    }
}
