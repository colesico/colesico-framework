package colesico.framework.assist.codegen.model;

import colesico.framework.assist.codegen.CodegenException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class FieldElement extends VarElement {

    protected final ClassElement parentClass;
    protected final VariableElement originVariableElement;

    public FieldElement(ProcessingEnvironment processingEnv, ClassElement parentClass, VariableElement fieldElement) {
        super(processingEnv);
        if (!fieldElement.getKind().isField()) {
            throw CodegenException.of().message("Unsupported element kind:" + fieldElement.getKind()).element(fieldElement).build();
        }

        this.parentClass = parentClass;
        this.originVariableElement = fieldElement;
    }

    @Override
    public VariableElement unwrap() {
        return originVariableElement;
    }

    public ClassElement getParentClass() {
        return parentClass;
    }

    @Override
    public String getName() {
        return originVariableElement.getSimpleName().toString();
    }

    @Override
    public TypeMirror asType() {
        return parentClass.asClassType().getMemberType(originVariableElement);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldElement that = (FieldElement) o;

        return originVariableElement != null ? originVariableElement.equals(that.originVariableElement) : that.originVariableElement == null;
    }

    @Override
    public int hashCode() {
        return originVariableElement != null ? originVariableElement.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FieldElement{" +
                "originVariableElement=" + originVariableElement +
                '}';
    }
}
