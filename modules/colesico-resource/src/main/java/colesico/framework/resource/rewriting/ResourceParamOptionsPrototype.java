package colesico.framework.resource.rewriting;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.resource.internal.ParamRewriter;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class ResourceParamOptionsPrototype {

    abstract public void configure(Options options);

    /**
     * Options for {@link ParamRewriter}.
     * It replaces parameter names in the path with their values
     * <p>
     * Example:
     * /foo/$param1/$param2/bar/baz for $param1=dummy; $param2=100 will be
     * rewrote to  /foo/dummy/100/bar/baz
     */
    public interface Options {

        /**
         * Set path param value
         *
         * @param name  parameter name, must be started with '$' char
         * @param value any string value
         */
        Options param(String name, String value);

        /**
         * Parameter value.
         *
         * @param name parameter name, must be started with '$' char
         */
        String getValue(String name);

    }
}
