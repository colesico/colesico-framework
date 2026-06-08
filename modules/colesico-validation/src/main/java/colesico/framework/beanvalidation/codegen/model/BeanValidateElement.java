package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.beanvalidation.ValidateBean;

/**
 * @see ValidateBean
 */
public class BeanValidateElement extends ValidateElement {

    /**
     * Validator builder that be used for this field validation
     */
    private BuilderPrototypeElement fieldValidatorBuilder;

    public BeanValidateElement(FieldElement originField, String subject, String mapper, BuilderPrototypeElement fieldValidatorBuilder) {
        super(originField, subject, mapper);
        this.fieldValidatorBuilder = fieldValidatorBuilder;
    }

    @Override
    public String validationMethodName() {
        return "validate" + StringUtils.firstCharToUpperCase(propertyName());
    }

    public String validatorBuilderFieldName() {
        return propertyName() + "VB";
    }

    public BuilderPrototypeElement fieldValidatorBuilder() {
        return fieldValidatorBuilder;
    }
}
