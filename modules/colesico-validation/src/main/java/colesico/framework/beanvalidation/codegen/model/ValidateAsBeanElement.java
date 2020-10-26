package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StrUtils;

public class ValidateAsBeanElement {

    private ValidatedPropertyElement parentProperty;

    private final boolean optional;

    public ValidateAsBeanElement(boolean optional) {
        this.optional = optional;
    }

    public boolean isOptional() {
        return optional;
    }

    public String validationBuilderGetterName() {
        return "validatorBuilder" + StrUtils.firstCharToUpperCase(parentProperty.getPropertyName());
    }

    public ValidatedPropertyElement getParentProperty() {
        return parentProperty;
    }

    public void setParentProperty(ValidatedPropertyElement parentProperty) {
        this.parentProperty = parentProperty;
    }


}
