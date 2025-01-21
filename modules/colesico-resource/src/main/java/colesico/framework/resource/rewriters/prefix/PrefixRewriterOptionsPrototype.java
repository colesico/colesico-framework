package colesico.framework.resource.rewriters.prefix;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class PrefixRewriterOptionsPrototype {
    abstract public void configure(PrefixRewriterSettings settings);
}
