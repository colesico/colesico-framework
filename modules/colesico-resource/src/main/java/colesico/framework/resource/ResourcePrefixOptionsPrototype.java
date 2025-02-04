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
     * It rewrites the resource name by a partial prefix match.
     * E.g for the rewriting  '/etc/srv'->'/foo'   resource name '/etc/srv/generator/x' will be rewritten to  '/foo/generator/x'
     */
    public interface Options {

        /**
         * Specify resource name prefix rewritings
         *
         * @param phase            supported phases: {@link Phase#BEFORE_LOCALIZE}, {@link Phase#AFTER_LOCALIZE}
         * @param originPrefix example:  "bar", "foo/dummy", "root/dir/subdir"
         * @param targetPrefix example:  "module", "module/dir"
         */
        Options substitution(String originPrefix, String targetPrefix, Phase phase);

        default Options substitution(String originPrefix, String targetPrefix) {
            return substitution(originPrefix, targetPrefix, Phase.BEFORE_LOCALIZE);
        }
    }

    /**
     *
     */
    public enum Phase {

        /**
         * Phase before base name localized
         */
        BEFORE_LOCALIZE,

        /**
         * Phase after base name localized
         */
        AFTER_LOCALIZE
    }
}
