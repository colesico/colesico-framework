package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StrUtils;
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

    public BeanValidationElement(FieldElement originField, String subject, ValidatorBuilderElement fieldValidatorBuilder) {
        super(originField, subject);
        this.fieldValidatorBuilder = fieldValidatorBuilder;
    }

    @Override
    public String getValidationMethodName() {
        return "validate" + StrUtils.firstCharToUpperCase(getPropertyName());
    }

    public String getValidatorBuilderFieldName() {
        return getPropertyName() + "Validation";
    }

    public ValidatorBuilderElement getFieldValidatorBuilder() {
        return fieldValidatorBuilder;
    }
}
