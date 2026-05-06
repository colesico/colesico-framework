package colesico.framework.teleapi.dataport;

import java.lang.reflect.Type;

/**
 * Value tele-read context.
 * @param <P> context payload type
 */
abstract public class TRContext<T extends Type, P> {

    /**
     * Target value type
     */
    protected final T valueType;

    /**
     * Any custom data that can be used by the {@link TeleReader}
     * to value specific read
     */
    protected final P payload;

    public TRContext(T valueType, P payload) {
        this.valueType = valueType;
        this.payload = payload;
    }

    public Type valueType() {
        return valueType;
    }

    public P payload() {
        return payload;
    }

}
