package colesico.framework.teleapi.dataport;

import java.lang.reflect.Type;

/**
 * Value tele-write context
 */
abstract public class TWContext<T extends Type, A> {

    /**
     * Result type
     */
    protected T valueType;

    /**
     * Any custom attributes
     * that can be used by the {@link TeleWriter}
     * to value specific write
     */
    protected A attributes;

    public TWContext(T valueType) {
        this.valueType = valueType;
    }

    public T valueType() {
        return valueType;
    }

    public void setValueType(T valueType) {
        this.valueType = valueType;
    }

    public A attributes() {
        return attributes;
    }

    public void setAttributes(A attributes) {
        this.attributes = attributes;
    }
}
