package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.ReadOptions;

/**
 * Basic read options for interaction via http
 */
public interface HttpReadOptions extends ReadOptions {

    /**
     * Http param name.
     * This can be a header or cookie or query param etc. name
     *
     * @see ParamName
     */
    String paramName();

    /**
     * Origin name to read value from it
     *
     * @see ParamOrigin
     */
    String originName();

}
