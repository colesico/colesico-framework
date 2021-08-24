package colesico.framework.teleapi;

import java.lang.reflect.Type;

/**
 * Basic tele data-reading context.
 */
abstract public class TRContext {

    /**
     * Target value type
     */
    protected Type valueType;

    public TRContext(Type valueType) {
        this.valueType = valueType;
    }

    public Type getValueType() {
        return valueType;
    }

    public void setValueType(Type valueType) {
        this.valueType = valueType;
    }
}
