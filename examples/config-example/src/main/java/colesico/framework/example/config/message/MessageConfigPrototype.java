package colesico.framework.example.config.message;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.MESSAGE, target = TargetBean.class)
abstract public class MessageConfigPrototype {
    abstract public String getValue();
}
