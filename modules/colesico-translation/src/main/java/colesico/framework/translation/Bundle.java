package colesico.framework.translation;

/**
 * Dictionary is used to perform text or messages translation.
 */
public interface Bundle {

    String GET_METHOD = "get";

    /**
     * Returns the string by its key or the default value if string was not found.
     * Also performs the parameter substitution with MessageFormat.format(...)
     *
     * @param key
     * @param params
     * @return
     */
    //TODO: specify forrmater via config?
    String get(String key, String defaultValue, Object... params);

}
