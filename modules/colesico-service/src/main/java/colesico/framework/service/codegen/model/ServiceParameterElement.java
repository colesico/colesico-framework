package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;

/**
 * Service method parameter element
 */
public class ServiceParameterElement {
    private final ServiceMethodElement parentMethod;
    private final VarElement originParam;

    public ServiceParameterElement(ServiceMethodElement parentMethod, VarElement originParam) {
        this.parentMethod = parentMethod;
        this.originParam = originParam;
    }

    public ServiceMethodElement parentMethod() {
        return parentMethod;
    }

    public VarElement originParam() {
        return originParam;
    }
}
