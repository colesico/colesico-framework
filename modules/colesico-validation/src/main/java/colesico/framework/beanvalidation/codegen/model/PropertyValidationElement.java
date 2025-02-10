package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.FieldElement;

import java.util.Set;

/**
 * @see colesico.framework.beanvalidation.Validate
 */
public class PropertyValidationElement extends ValidationElement {

    private final Boolean verifier;

    public PropertyValidationElement(FieldElement originField, String subject, String command, Boolean verifier) {
        super(originField, subject, command);
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
