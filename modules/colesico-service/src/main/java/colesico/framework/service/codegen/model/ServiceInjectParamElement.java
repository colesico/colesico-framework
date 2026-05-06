package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;

import javax.lang.model.type.TypeMirror;

public class ServiceInjectParamElement extends ServiceParameterElement {

    private String named;
    private TypeMirror classed;

    public ServiceInjectParamElement(ServiceMethodElement parentMethod, VarElement originParam) {
        super(parentMethod, originParam);
    }

    public String named() {
        return named;
    }

    public void setNamed(String named) {
        this.named = named;
    }

    public TypeMirror classed() {
        return classed;
    }

    public void setClassed(TypeMirror classed) {
        this.classed = classed;
    }
}
