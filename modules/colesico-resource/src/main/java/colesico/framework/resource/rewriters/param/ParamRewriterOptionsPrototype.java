package colesico.framework.resource.rewriters.param;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class ParamRewriterOptionsPrototype {

    abstract public void configure(ParamRewriterSettings settings);

}
