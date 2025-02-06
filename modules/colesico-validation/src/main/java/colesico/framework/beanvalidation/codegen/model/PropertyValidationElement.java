package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.FieldElement;

/**
 * @see colesico.framework.beanvalidation.Validate
 */
public class PropertyValidationElement extends ValidationElement {

    private final Boolean verifier;

    public PropertyValidationElement(FieldElement originField, String subject, Boolean verifier) {
        super(originField, subject);
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
