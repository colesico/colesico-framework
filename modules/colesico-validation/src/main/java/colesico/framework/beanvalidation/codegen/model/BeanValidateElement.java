package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.beanvalidation.BeanValidate;

/**
 * @see BeanValidate
 */
public class BeanValidateElement extends ValidateElement {

    /**
     * Validator builder that be used for this field validation
     */
    private ValidatorBuilderElement fieldValidatorBuilder;

    public BeanValidateElement(FieldElement originField, String subject, String mapper, ValidatorBuilderElement fieldValidatorBuilder) {
        super(originField, subject, mapper);
        this.fieldValidatorBuilder = fieldValidatorBuilder;
    }

    @Override
    public String validationMethodName() {
        return "validate" + StringUtils.firstCharToUpperCase(fieldName());
    }

    public String validatorBuilderFieldName() {
        return fieldName() + "Validation";
    }

    public ValidatorBuilderElement fieldValidatorBuilder() {
        return fieldValidatorBuilder;
    }
}
