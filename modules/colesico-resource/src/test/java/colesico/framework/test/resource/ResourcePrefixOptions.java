package colesico.framework.test.resource;

import colesico.framework.config.Config;
import colesico.framework.resource.ResourcePrefixOptionsPrototype;

@Config
public class ResourcePrefixOptions extends ResourcePrefixOptionsPrototype {
    @Override
    public void configure(Options options) {
        options
                .addPrefixSubstitution("alias", "foo/dummy")
                .addPrefixSubstitution("alias/ok","bar/bazz");
    }
}
