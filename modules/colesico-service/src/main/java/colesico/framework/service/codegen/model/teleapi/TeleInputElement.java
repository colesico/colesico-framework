package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents tele-command variable: parameter, batch param, injected param...
 */
abstract public class TeleInputElement {

    /**
     * Parent tele-command ref
     */
    protected final TeleCommandElement parentTeleCommand;

    /**
     * Optional origin element
     */
    protected final VarElement originElement;

    /**
     * Custom purpose props
     */
    protected final Map<Class<?>, Object> properties = new HashMap<>();

    public TeleInputElement(TeleCommandElement parentTeleCommand, VarElement originElement) {
        this.parentTeleCommand = parentTeleCommand;
        this.originElement = originElement;
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

    public VarElement originElement() {
        return originElement;
    }
}
