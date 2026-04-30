package colesico.framework.teleapi.dataport;

import java.lang.reflect.Type;

/**
 * Value tele-write context
 */
abstract public class TWContext<T extends Type, A> {

    /**
     * Type of value to be written
     */
    protected final T valueType;

    /**
     * Any custom attributes
     * that can be used by the {@link TeleWriter}
     * to value specific write
     */
    protected final A attributes;

    public TWContext(T valueType, A attributes) {
        this.valueType = valueType;
        this.attributes = attributes;
    }

    public T valueType() {
        return valueType;
    }

    public A attributes() {
        return attributes;
    }

}
