package colesico.framework.resource;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.resource.internal.PrefixRewriter;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class ResourcePrefixOptionsPrototype {

    /**
     * To use in code generators
     */
    public static final String CONFIGURE_METHOD = "configure";

    abstract public void configure(Options options);

    /**
     * Settings for {@link PrefixRewriter}.
     * It rewrites the path by a partial prefix match.
     * E.g for the rewriting  '/etc/srv'->'/foo'   path '/etc/srv/generator/x' will be rewritten to  '/foo/generator/x'
     */
    public interface Options {

        /**
         * Specify pat prefix rewritings
         *
         * @param phase            supported phases: {@link RewritingPhase#EVALUATE}, {@link RewritingPhase#SUBSTITUTE}
         * @param originPathPrefix example:  "bar", "foo/dummy", "root/dir/subdir"
         * @param targetPathPrefix example:  "module", "module/dir"
         */
        Options addRewriting(String originPathPrefix, String targetPathPrefix, RewritingPhase phase);

        default Options addRewriting(String originPathPrefix, String targetPathPrefix) {
            return addRewriting(originPathPrefix, targetPathPrefix, RewritingPhase.EVALUATE);
        }
    }
}
