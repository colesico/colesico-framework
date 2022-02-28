package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents any entity related to tele method  (parameter, compound, batch field, e.t.c.)
 */
abstract public class TeleMethodRelatedElement {

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

    public TeleMethodRelatedElement(TeleMethodElement parentTeleMethod, VarElement originElement) {
        this.parentTeleMethod = parentTeleMethod;
        this.originElement = originElement;
    }

    public <C> C getProperty(Class<C> propertyClass) {
        return (C) properties.get(propertyClass);
    }

    public void setProperty(Class<?> propertyClass, Object property) {
        properties.put(propertyClass, property);
    }

    public TeleMethodElement getParentTeleMethod() {
        return parentTeleMethod;
    }

    public VarElement getOriginElement() {
        return originElement;
    }
}
