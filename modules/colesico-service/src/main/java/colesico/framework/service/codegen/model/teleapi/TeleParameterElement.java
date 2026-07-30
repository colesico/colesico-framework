package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents tele-command parameter
 */
abstract public class TeleParameterElement {

    /**
     * Parent tele-command ref
     */
    protected final TeleCommandElement parentTeleCommand;

    /**
     * Origin method param or bean field
     */
    protected final VarElement originVariable;

    /**
     * Custom purpose props
     */
    protected final Map<Class<?>, Object> properties = new HashMap<>();

    public TeleParameterElement(TeleCommandElement parentTeleCommand, VarElement originVariable) {
        this.parentTeleCommand = parentTeleCommand;
        this.originVariable = originVariable;
    }

    public <C> C property(Class<C> propertyClass) {
        return (C) properties.get(propertyClass);
    }

    public void setProperty(Class<?> propertyClass, Object property) {
        properties.put(propertyClass, property);
    }

    public TeleCommandElement parentTeleCommand() {
        return parentTeleCommand;
    }

    public VarElement originVariable() {
        return originVariable;
    }
}
