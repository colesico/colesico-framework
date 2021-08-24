package colesico.framework.teleapi;

import java.lang.reflect.Type;

/**
 * Basic tele data-writing context
 */
abstract public class TWContext {

    /**
     * Result type
     */
    protected Type valueType;

    public TWContext(Type valueType) {
        this.valueType = valueType;
    }

    public Type getValueType() {
        return valueType;
    }

    public void setValueType(Type valueType) {
        this.valueType = valueType;
    }
}
