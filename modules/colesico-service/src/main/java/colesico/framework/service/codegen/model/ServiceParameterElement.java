package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;

/**
 * Service method parameter element
 */
public class ServiceParameterElement {

    private final ServiceMethodElement parentMethod;

    private final VarElement originParameter;

    public ServiceParameterElement(ServiceMethodElement parentMethod, VarElement originParameter) {
        this.parentMethod = parentMethod;
        this.originParameter = originParameter;
    }

    public ServiceMethodElement parentMethod() {
        return parentMethod;
    }

    public VarElement originParameter() {
        return originParameter;
    }
}
