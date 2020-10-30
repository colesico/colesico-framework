package colesico.framework.beanvalidation.codegen.model;

public class ValidateWithBuilderElement {

    private ValidatedPropertyElement parentProperty;

    /**
     * Validator builder full class name
     */
    private String builderClass;

    private final boolean optional;

    public ValidateWithBuilderElement(String builderClass, boolean optional) {
        this.builderClass = builderClass;
        this.optional = optional;
    }

    public boolean isOptional() {
        return optional;
    }

    public String validatorFieldName() {
        return parentProperty.getPropertyName() + "BVB";
    }

    public ValidatedPropertyElement getParentProperty() {
        return parentProperty;
    }

    public void setParentProperty(ValidatedPropertyElement parentProperty) {
        this.parentProperty = parentProperty;
    }

    public String getBuilderClass() {
        return builderClass;
    }
}
