package colesico.framework.example.config.defaultcfg;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class DefaultConfigPrototype {
    public abstract String configure();
}
