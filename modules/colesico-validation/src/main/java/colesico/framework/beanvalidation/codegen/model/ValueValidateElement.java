package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.beanvalidation.Validate;

/**
 * @see colesico.framework.beanvalidation.Validate
 */
public class ValueValidateElement extends ValidateElement {

    /**
     * Validation method name
     *
     * @see Validate#method()
     */
    private final String method;

    private final Boolean verifier;

    public ValueValidateElement(FieldElement originField, String subject, String mapper, String method, Boolean verifier) {
        super(originField, subject, mapper);
        this.method = method;
        this.verifier = verifier;
    }

    public String subject() {
        return subject;
    }

    public Boolean verifier() {
        return verifier;
    }

    @Override
    public String validationMethodName() {
        if (StringUtils.isBlank(method)) {
            if (verifier) {
                return "verify" + StringUtils.firstCharToUpperCase(originField().name());
            }
            return "validate" + StringUtils.firstCharToUpperCase(originField().name());
        }

        return method;

    }

}
