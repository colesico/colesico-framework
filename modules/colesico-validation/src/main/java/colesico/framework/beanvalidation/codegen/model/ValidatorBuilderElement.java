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

    public ClassElement getOriginClass() {
        return originClass;
    }

    public String getPackageName() {
        return originClass.packageName();
    }

    public ClassType getPrototypeType() {
        return prototypeType;
    }
}
