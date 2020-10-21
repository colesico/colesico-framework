package colesico.framework.beanvalidator.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.FieldElement;

import javax.lang.model.type.TypeMirror;

public class ValidatedPropertyElement {

    /**
     * Parent validator builder element
     */
    protected ValidatorBuilderElement parentBuilder;

    protected final FieldElement originField;

    private final Boolean verifier;

    public ValidatedPropertyElement(FieldElement originField, Boolean verifier) {
        this.originField = originField;
        this.verifier = verifier;
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
          return "get"+StrUtils.firstCharToUpperCase(getPropertyName());
    }

    public String getValidateMethodName(){
        return "validate"+ StrUtils.firstCharToUpperCase(getPropertyName());
    }

    public String getVerifyMethodName(){
        return "verify"+ StrUtils.firstCharToUpperCase(getPropertyName());
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
}
