package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;

public class ValidatorBuilderElement {

    private final ClassElement originClass;
    private final ClassType prototypeType;

    public ValidatorBuilderElement(ClassElement originClass, ClassType prototypeType) {
        this.originClass = originClass;
        this.prototypeType = prototypeType;
    }

    public ClassElement originClass() {
        return originClass;
    }

    public String packageName() {
        return originClass.packageName();
    }

    public ClassType prototypeType() {
        return prototypeType;
    }
}
