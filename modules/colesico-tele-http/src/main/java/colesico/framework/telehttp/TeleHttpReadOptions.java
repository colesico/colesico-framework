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
     * Origin name to read value from it
     *
     * @see ParamOrigin
     */
    String originName();

    /**
     *  Overrides the default reader to be used for reading the value
     */
    Class<? extends TeleHttpReader<?, ?>> readerClass();
}
