package colesico.framework.example.config.classed;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class PolyConfigPrototype {
    abstract public String getValue();
}
