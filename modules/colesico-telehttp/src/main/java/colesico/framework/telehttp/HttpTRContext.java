package colesico.framework.telehttp;

import java.lang.reflect.Type;

/**
 * Basic tele-reading context for interaction via http
 */
abstract public class HttpTRContext {

    /**
     * Http param name.
     * This can be a header or cookie or query param etc. name
     *
     * @see ParamName
     */
    private final String paramName;

    /**
     * Target value type
     */
    private Type valueType;

    /**
     * Origin name to read value from it
     */
    private final String originName;

    public HttpTRContext(String paramName, String originName) {
        this.paramName = paramName;
        this.originName = originName;
    }

    public static HttpTRContext instance(String name, String originName) {
        return new HttpTRContext(name, originName) {
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

    public Type getValueType() {
        return valueType;
    }

    public void setValueType(Type valueType) {
        this.valueType = valueType;
    }
}
