package colesico.framework.teleapi.dataport;

import java.lang.reflect.Type;

/**
 * Value tele-write context
 */
abstract public class TWContext<T extends Type, P> {

    /**
     * Type of value to be written
     */
    protected final T valueType;

    /**
     * Any custom data
     * that can be used by the {@link TeleWriter}
     * to value specific write
     */
    protected final P payload;

    public TWContext(T valueType, P payload) {
        this.valueType = valueType;
        this.payload = payload;
    }

    public T valueType() {
        return valueType;
    }

    public P payload() {
        return payload;
    }

}
