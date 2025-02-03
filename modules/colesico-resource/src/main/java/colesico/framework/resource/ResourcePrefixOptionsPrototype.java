package colesico.framework.resource;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.resource.internal.PrefixSubstitutor;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class ResourcePrefixOptionsPrototype {

    /**
     * To use in code generators
     */
    public static final String CONFIGURE_METHOD = "configure";

    abstract public void configure(Options options);

    /**
     * Settings for {@link PrefixSubstitutor}.
     * It rewrites the path by a partial prefix match.
     * E.g for the rewriting  '/etc/srv'->'/foo'   path '/etc/srv/generator/x' will be rewritten to  '/foo/generator/x'
     */
    public interface Options {

        /**
         * Specify pat prefix rewritings
         *
         * @param phase            supported phases: {@link Phase#BEFORE_LOCALIZE}, {@link Phase#AFTER_LOCALIZE}
         * @param originPathPrefix example:  "bar", "foo/dummy", "root/dir/subdir"
         * @param targetPathPrefix example:  "module", "module/dir"
         */
        Options addPrefixSubstitution(String originPathPrefix, String targetPathPrefix, Phase phase);

        default Options addPrefixSubstitution(String originPathPrefix, String targetPathPrefix) {
            return addPrefixSubstitution(originPathPrefix, targetPathPrefix, Phase.BEFORE_LOCALIZE);
        }
    }

    /**
     *
     */
    public enum Phase {

        /**
         * Phase to evaluate path expressions
         */
        BEFORE_LOCALIZE,

        /**
         * Phase to substitute/redirect path
         */
        AFTER_LOCALIZE
    }
}
