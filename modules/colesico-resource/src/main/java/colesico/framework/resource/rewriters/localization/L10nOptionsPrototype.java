package colesico.framework.resource.rewriters.localization;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class L10nOptionsPrototype {
    abstract public void configure(L10nSettings settings);
}
