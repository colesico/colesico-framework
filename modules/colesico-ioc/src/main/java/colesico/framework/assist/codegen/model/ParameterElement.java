package colesico.framework.assist.codegen.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class ParameterElement extends VarElement {

    protected final MethodElement parentMethod;
    protected final VariableElement originVariableElement;
    protected final TypeMirror originTypeMirror;

    public ParameterElement(ProcessingEnvironment processingEnv, MethodElement parentMethod, VariableElement parameterElement, TypeMirror parameterType) {
        super(processingEnv);
        this.parentMethod = parentMethod;
        this.originVariableElement = parameterElement;
        this.originTypeMirror = parameterType;
    }

    @Override
    public VariableElement unwrap() {
        return originVariableElement;
    }

    @Override
    public String getName() {
        return originVariableElement.getSimpleName().toString();
    }

    @Override
    public TypeMirror asType() {
        return originTypeMirror;
    }

    public MethodElement getParentMethod() {
        return parentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterElement that = (ParameterElement) o;

        return originVariableElement != null ? originVariableElement.equals(that.originVariableElement) : that.originVariableElement == null;
    }

    @Override
    public int hashCode() {
        return originVariableElement != null ? originVariableElement.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ParameterElement{" +
                "originVariableElement=" + originVariableElement +
                ", originTypeMirror=" + originTypeMirror +
                '}';
    }
}
