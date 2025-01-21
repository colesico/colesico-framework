package colesico.framework.resource.rewriters.param;

/**
 * Settings for {@link ParamRewriter}.
 * It replaces parameter names in the path with their values
 * <p>
 * Example:
 * /foo/$param1/$param2/bar/baz for $param1=dummy; $param2=100 will be
 * rewrote to /foo/dummy/100/bar/baz
 */
public interface ParamRewriterSettings {

    /**
     * Set path param value
     *
     * @param name  parameter name, must be started with '$' char
     * @param value any string value
     */
    ParamRewriterSettings param(String name, String value);

    /**
     * Parameter value.
     *
     * @param name parameter name, must be started with '$' char
     */
    String getValue(String name);

}
