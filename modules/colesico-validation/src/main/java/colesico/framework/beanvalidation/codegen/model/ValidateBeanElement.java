package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

public class ValidateBeanElement {
    private final ClassType builder;
    private final boolean optional;

    public ValidateBeanElement(ClassType builder, boolean optional) {
        this.builder = builder;
        this.optional = optional;
    }

    public ClassType getBuilder() {
        return builder;
    }

    public boolean isOptional() {
        return optional;
    }
}
