package colesico.framework.test.resource;

import colesico.framework.config.Config;
import colesico.framework.resource.rewriters.param.ParamRewriterOptionsPrototype;
import colesico.framework.resource.rewriters.param.ParamRewriterSettings;

@Config
public class ParamRewriterOptions extends ParamRewriterOptionsPrototype {
    @Override
    public void configure(ParamRewriterSettings settings) {
        settings.param("$alias", "foo/dummy");
    }
}
