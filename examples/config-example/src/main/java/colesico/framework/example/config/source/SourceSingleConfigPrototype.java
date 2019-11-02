package colesico.framework.example.config.source;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class SourceSingleConfigPrototype {
    abstract public String getValue();
}
