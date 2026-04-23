package colesico.framework.teleapi.dataport;

import java.lang.reflect.Type;

/**
 * Basic tele data write context
 */
abstract public class TWContext {

    /**
     * Result type
     */
    protected Type valueType;

    public TWContext(Type valueType) {
        this.valueType = valueType;
    }

    public Type valueType() {
        return valueType;
    }

    public void setValueType(Type valueType) {
        this.valueType = valueType;
    }
}
