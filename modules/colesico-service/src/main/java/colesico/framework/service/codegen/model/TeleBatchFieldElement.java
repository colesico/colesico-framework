package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;

public class TeleBatchFieldElement extends TeleInputElement {

    private TeleBatchElement parentBatch;

    public TeleBatchFieldElement(VarElement originArg) {
        super(originArg);
    }

    public TeleBatchElement getParentBatch() {
        return parentBatch;
    }

    public void setParentBatch(TeleBatchElement parentBatch) {
        this.parentBatch = parentBatch;
    }
}
