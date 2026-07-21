package colesico.framework.test.resource;

import colesico.framework.config.Config;
import colesico.framework.resource.ResourceOptionsPrototype;

@Config
public class ResourceOptions extends ResourceOptionsPrototype {
    @Override
    public void configure(Options options) {
        options
                .substitution("alias", "foo/dummy")
                .substitution("alias/ok","bar/bazz");
    }
}
