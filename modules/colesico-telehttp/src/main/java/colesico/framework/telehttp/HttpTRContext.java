package colesico.framework.telehttp;

import colesico.framework.teleapi.TRContext;

import java.lang.reflect.Type;

/**
 * Basic tele-reading context for interaction via http
 */
abstract public class HttpTRContext extends TRContext {

    /**
     * Http param name.
     * This can be a header or cookie or query param etc. name
     *
     * @see ParamName
     */
    private final String paramName;

    /**
     * Origin name to read value from it
     */
    private final String originName;

    protected HttpTRContext(Type valueType, String paramName, String originName) {
        super(valueType);
        this.paramName = paramName;
        this.originName = originName;
    }

    public static HttpTRContext instance(Type valueType, String paramName, String originName) {
        return new HttpTRContext(valueType, paramName, originName) {
        };
    }

    /**
     * Http value name
     */
    public final String getParamName() {
        return paramName;
    }

    /**
     * Origin name
     */
    public String getOriginName() {
        return originName;
    }

}
