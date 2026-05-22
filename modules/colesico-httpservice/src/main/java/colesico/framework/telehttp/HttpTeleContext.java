package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.ReadOptions;

import java.lang.reflect.Type;

/**
 * Basic tele-reading context for interaction via http
 *
 * @param <P> context payload type
 */
abstract public class HttpTeleContext<T extends Type, P> extends ReadOptions<T, P> {

    /**
     * Http param name.
     * This can be a header or cookie or query param etc. name
     *
     * @see ParamName
     */
    private final String paramName;

    /**
     * Origin name to read value from it
     *
     * @see ParamOrigin
     */
    private final String originName;

    public HttpTeleContext(T valueType, String paramName, String originName, P payload) {
        super(valueType, payload);
        this.paramName = paramName;
        this.originName = originName;
    }

    /**
     * Http value name
     */
    public final String paramName() {
        return paramName;
    }

    /**
     * Origin name
     */
    public String originName() {
        return originName;
    }

}
