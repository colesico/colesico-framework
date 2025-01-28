package colesico.framework.test.resource;

import colesico.framework.config.Config;
import colesico.framework.resource.rewriting.ResourceParamOptionsPrototype;

@Config
public class ResourceParamOptions extends ResourceParamOptionsPrototype {
    @Override
    public void configure(Options options) {
        options.param("$alias", "foo/dummy");
    }
}
