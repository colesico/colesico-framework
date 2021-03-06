package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.FieldElement;

import javax.lang.model.type.TypeMirror;

public class ValidatedPropertyElement {

    /**
     * Parent validator builder element
     */
    protected ValidatorBuilderElement parentBuilder;

    protected final FieldElement originField;

    protected final String subject;

    protected final String methodName;

    private final Boolean verifier;

    private final ValidateWithBuilderElement validateWithBuilder;

    public ValidatedPropertyElement(FieldElement originField, String subject, String methodName, Boolean verifier, ValidateWithBuilderElement validateWithBuilder) {
        this.originField = originField;
        this.subject = subject;
        this.methodName = methodName;
        this.verifier = verifier;
        this.validateWithBuilder = validateWithBuilder;
        if (validateWithBuilder != null) {
            validateWithBuilder.setParentProperty(this);
        }
    }

    public final String getPropertyName() {
        if (originField != null) {
            return originField.getName();
        }
        return null;
    }

    public final TypeMirror getPropertyType() {
        if (originField != null) {
            return originField.getOriginType();
        }
        return null;
    }

    public final String getPropertyGetterName() {
        return "get" + StrUtils.firstCharToUpperCase(getPropertyName());
    }

    public String getSubject() {
        return subject;
    }

    public String getMethodName() {
        if (methodName == null) {
            String prefix;
            if (verifier) {
                prefix = "verify";
            } else {
                prefix = "validate";
            }
            return prefix + StrUtils.firstCharToUpperCase(getPropertyName());
        }
        return methodName;
    }

    public ValidatorBuilderElement getParentBuilder() {
        return parentBuilder;
    }

    public void setParentBuilder(ValidatorBuilderElement parentBuilder) {
        this.parentBuilder = parentBuilder;
    }

    public FieldElement getOriginField() {
        return originField;
    }

    public Boolean getVerifier() {
        return verifier;
    }

    public ValidateWithBuilderElement getValidateWithBuilder() {
        return validateWithBuilder;
    }
}
