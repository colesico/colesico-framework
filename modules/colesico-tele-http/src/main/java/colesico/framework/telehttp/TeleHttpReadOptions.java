package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.ReadOptions;

/**
 * Basic read options for interaction via http
 */
public interface TeleHttpReadOptions extends ReadOptions {

    /**
     * Http param name.
     * This can be a header or cookie or query param etc. name
     *
     * @see ParamName
     */
    String paramName();

    /**
     * Origin name to read actualResponse from it
     *
     * @see ParamOrigin
     */
    String originName();

}
