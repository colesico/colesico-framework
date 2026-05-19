package colesico.framework.security.assist.authentication;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class SimpleAuthConfigPrototype {
    abstract protected Integer maxAuthenticated();
}
