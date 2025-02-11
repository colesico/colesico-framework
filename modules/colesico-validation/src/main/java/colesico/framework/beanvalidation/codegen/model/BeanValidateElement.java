package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.beanvalidation.ValidateBean;

/**
 * @see ValidateBean
 */
public class BeanValidateElement extends ValidateElement {

    /**
     * Validator builder that be used for this field validation
     */
    private ValidatorBuilderPrototypeElement fieldValidatorBuilder;

    public BeanValidateElement(FieldElement originField, String subject, String mapper, ValidatorBuilderPrototypeElement fieldValidatorBuilder) {
        super(originField, subject, mapper);
        this.fieldValidatorBuilder = fieldValidatorBuilder;
    }

    @Override
    public String getValidationMethodName() {
        return "validate" + StrUtils.firstCharToUpperCase(getPropertyName());
    }

    public String getValidatorBuilderFieldName() {
        return getPropertyName() + "VB";
    }

    public ValidatorBuilderPrototypeElement getFieldValidatorBuilder() {
        return fieldValidatorBuilder;
    }
}
