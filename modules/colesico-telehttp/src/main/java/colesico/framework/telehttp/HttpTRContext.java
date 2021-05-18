package colesico.framework.telehttp;

import java.lang.reflect.Type;

/**
 * Basic tele-reading context for interaction via http
 */
abstract public class HttpTRContext {

    /**
     * Parameter name
     */
    private final String name;

    /**
     * Parameter type
     */
    private Type valueType;

    /**
     * Origin name to read parameter from it
     */
    private final String originName;

    public HttpTRContext(String name, String originName) {
        this.name = name;
        this.originName = originName;
    }

    public static HttpTRContext of(String name, String originName) {
        return new HttpTRContext(name, originName) {
        };
    }

    /**
     * Parameter name
     */
    public final String getName() {
        return name;
    }

    /**
     * Origin class
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
