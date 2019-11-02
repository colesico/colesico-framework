package colesico.framework.example.config.single;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class SingleConfigPrototype {
    abstract public String getValue();
}
