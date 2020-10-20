package colesico.framework.dslvalidator.codegen.model;

import colesico.framework.assist.codegen.model.FieldElement;

public class ValidatedPropertyElement {

    /**
     * Parent validator builder prototype element
     */
    protected ValidatorBuilderPrototypeElement parentVBP;

    protected final FieldElement originField;

    private final Boolean verifier;

    public ValidatedPropertyElement(FieldElement originField, Boolean verifier) {
        this.originField = originField;
        this.verifier = verifier;
    }

    public ValidatorBuilderPrototypeElement getParentVBP() {
        return parentVBP;
    }

    public void setParentVBP(ValidatorBuilderPrototypeElement parentVBP) {
        this.parentVBP = parentVBP;
    }

    public FieldElement getOriginField() {
        return originField;
    }

    public Boolean getVerifier() {
        return verifier;
    }
}
