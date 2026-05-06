package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.VarElement;
import colesico.framework.service.codegen.model.ServiceParameterElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents any tele-command parameter (batch param, injected param...)
 */
abstract public class TeleParameterElement {

    /**
     * Parent tele-command ref
     */
    protected final TeleCommandElement parentTeleCommand;

    /**
     * Origin parameter
     */
    protected final ServiceParameterElement serviceParameter;

    /**
     * Custom purpose props
     */
    protected final Map<Class<?>, Object> properties = new HashMap<>();

    public TeleParameterElement(TeleCommandElement parentTeleCommand, ServiceParameterElement serviceParameter) {
        this.parentTeleCommand = parentTeleCommand;
        this.serviceParameter = serviceParameter;
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

    public ServiceParameterElement serviceParameter() {
        return serviceParameter;
    }

    public VarElement originElement() {
        return serviceParameter.originParameter();
    }
}
