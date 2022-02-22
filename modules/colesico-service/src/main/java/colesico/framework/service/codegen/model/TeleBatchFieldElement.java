package colesico.framework.service.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.VarElement;

public class TeleBatchFieldElement extends TeleInputElement {

    private TeleBatchElement parentBatch;

    /**
     * Batch field name
     */
    private String name;

    public TeleBatchFieldElement(TeleMethodElement parentTeleMethod, VarElement originElement, String name) {
        super(parentTeleMethod, originElement);
        checkFieldName(name);
        this.name = name;
    }

    private void checkFieldName(String name) {
        //TODO
    }

    public String getterName() {
        return "get" + StrUtils.firstCharToUpperCase(getName());
    }

    public String setterName() {
        return "set" + StrUtils.firstCharToUpperCase(getName());
    }

    public TeleBatchElement getParentBatch() {
        return parentBatch;
    }

    public void setParentBatch(TeleBatchElement parentBatch) {
        this.parentBatch = parentBatch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
