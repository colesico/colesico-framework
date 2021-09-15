package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Represents tele method parameter or  field or compound parameter field
 */
abstract public class TeleArgumentElement {

    /**
     * Parent tele-method ref
     */
    protected TeleMethodElement parentTeleMethod;

    /**
     * Parent compound if exists
     */
    protected TeleCompoundElement parentCompound;

    /**
     * Origin element ref  (method param or comp field)
     */
    protected final VarElement originElement;

    /**
     * Custom purpose props
     */
    private final Map<Class<?>, Object> properties = new HashMap<>();

    public TeleArgumentElement(VarElement originArg) {
        this.originElement = originArg;
    }

    public <C> C getProperty(Class<C> propertyClass) {
        return (C) properties.get(propertyClass);
    }

    public void setProperty(Class<?> propertyClass, Object property) {
        properties.put(propertyClass, property);
    }

    public VarElement getOriginElement() {
        return originElement;
    }

    public TeleMethodElement getParentTeleMethod() {
        return parentTeleMethod;
    }

    public void setParentTeleMethod(TeleMethodElement parentTeleMethod) {
        this.parentTeleMethod = parentTeleMethod;
    }

    public TeleCompoundElement getParentCompound() {
        return parentCompound;
    }

    public void setParentCompound(TeleCompoundElement parentCompound) {
        this.parentCompound = parentCompound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeleArgumentElement that = (TeleArgumentElement) o;
        return originElement.equals(that.originElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originElement);
    }
}
