package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.FieldElement;

/**
 * @see colesico.framework.beanvalidation.Validate
 */
public class PropertyValidateElement extends ValidateElement {

    private final Boolean verifier;

    public PropertyValidateElement(FieldElement originField, String subject, String mapper, Boolean verifier) {
        super(originField, subject, mapper);
        this.verifier = verifier;
    }

    public String getSubject() {
        return subject;
    }

    public Boolean getVerifier() {
        return verifier;
    }

    @Override
    public String getValidationMethodName() {
        String prefix;
        if (verifier) {
            prefix = "verify";
        } else {
            prefix = "validate";
        }
        return prefix + StrUtils.firstCharToUpperCase(getPropertyName());
    }

}
