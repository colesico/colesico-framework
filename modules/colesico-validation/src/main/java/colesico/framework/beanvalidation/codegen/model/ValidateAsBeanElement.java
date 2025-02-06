package colesico.framework.beanvalidation.codegen.model;

/**
 * @see colesico.framework.beanvalidation.ValidateAsBean
 */
public class ValidateAsBeanElement {

    private ValidatedPropertyElement parentProperty;

    /**
     * Validator builder full class name
     */
    private String builderClass;

    private final boolean optional;

    public ValidateAsBeanElement(String builderClass, boolean optional) {
        this.builderClass = builderClass;
        this.optional = optional;
    }

    public boolean isOptional() {
        return optional;
    }

    public String validatorFieldName() {
        return parentProperty.getPropertyName() + "VB";
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
