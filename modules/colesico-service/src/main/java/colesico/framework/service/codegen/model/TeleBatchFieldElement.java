package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;

public class TeleBatchFieldElement extends TeleVarElement{

    private TeleBatchElement parentBatch;

    public TeleBatchFieldElement(VarElement originArg, TeleBatchElement parentBatch) {
        super(originArg);
        this.parentBatch = parentBatch;
    }

    public TeleBatchElement getParentBatch() {
        return parentBatch;
    }

    public void setParentBatch(TeleBatchElement parentBatch) {
        this.parentBatch = parentBatch;
    }
}
