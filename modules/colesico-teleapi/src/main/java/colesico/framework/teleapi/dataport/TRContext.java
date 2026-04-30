package colesico.framework.teleapi.dataport;

import java.lang.reflect.Type;

/**
 * Value tele-read context.
 */
abstract public class TRContext<T extends Type, A> {

    /**
     * Target value type
     */
    protected final T valueType;

    /**
     * Any custom attributes
     * that can be used by the {@link TeleReader}
     * to value specific read
     */
    protected A attributes;

    public TRContext(T valueType) {
        this.valueType = valueType;
    }

    public TRContext(T valueType, A attributes) {
        this.valueType = valueType;
        this.attributes = attributes;
    }

    public Type valueType() {
        return valueType;
    }

    public A attributes() {
        return attributes;
    }

    public void setAttributes(A attributes) {
        this.attributes = attributes;
    }
}
