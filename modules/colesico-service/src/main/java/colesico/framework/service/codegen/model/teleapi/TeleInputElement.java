package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents tele-method variable: parameter, batch param
 */
abstract public class TeleInputElement {

    /**
     * Parent tele-method ref
     */
    protected final TeleMethodElement parentTeleMethod;

    /**
     * Optional origin element
     */
    protected final VarElement originElement;

    /**
     * Custom purpose props
     */
    protected final Map<Class<?>, Object> properties = new HashMap<>();

    public TeleInputElement(TeleMethodElement parentTeleMethod, VarElement originElement) {
        this.parentTeleMethod = parentTeleMethod;
        this.originElement = originElement;
    }

    public <C> C property(Class<C> propertyClass) {
        return (C) properties.get(propertyClass);
    }

    public void setProperty(Class<?> propertyClass, Object property) {
        properties.put(propertyClass, property);
    }

    public TeleMethodElement parentTeleMethod() {
        return parentTeleMethod;
    }

    public VarElement originElement() {
        return originElement;
    }
}
