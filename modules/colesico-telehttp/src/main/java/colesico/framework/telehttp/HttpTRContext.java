package colesico.framework.telehttp;

import java.lang.reflect.Type;

/**
 * Basic tele-reading context for interaction via http
 */
abstract public class HttpTRContext {

    /**
     * Http value entity name.
     * This can be a header or cookie or query param etc. name
     */
    private final String name;

    /**
     * Target value type
     */
    private Type valueType;

    /**
     * Origin name to read value from it
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
     * Http value name
     */
    public final String getName() {
        return name;
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
