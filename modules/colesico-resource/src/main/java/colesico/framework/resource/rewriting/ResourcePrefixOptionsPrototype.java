package colesico.framework.resource.rewriting;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.resource.internal.PrefixRewriter;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class ResourcePrefixOptionsPrototype {
    abstract public void configure(Options options);

    /**
     * Settings for {@link PrefixRewriter}.
     * It rewrites the path by a partial prefix match.
     * E.g for the rewriting  '/etc/srv'->'/foo'   path '/etc/srv/generator/x' will be rewritten to  '/foo/generator/x'
     */
    public interface Options {
        Options rewriting(String originPathPrefix, String targetPathPrefix);
    }
}
