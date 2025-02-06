package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.beanvalidation.ValidateBean;

/**
 * @see ValidateBean
 */
public class BeanValidationElement extends ValidationElement {

    /**
     * Validator builder that be used for this field validation
     */
    private ValidatorBuilderElement fieldValidatorBuilder;

    public BeanValidationElement(FieldElement originField, ValidatorBuilderElement fieldValidatorBuilder) {
        super(originField);
        this.fieldValidatorBuilder = fieldValidatorBuilder;
    }

    public String validatorFieldName() {
        return getPropertyName() + "Validation";
    }

    public ValidatorBuilderElement getFieldValidatorBuilder() {
        return fieldValidatorBuilder;
    }
}
