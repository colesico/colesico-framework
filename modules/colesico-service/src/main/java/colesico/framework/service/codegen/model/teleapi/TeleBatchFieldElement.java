package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.VarElement;

/**
 * Represents field of batch class and corresponding method parameter
 *
 * @see colesico.framework.service.BatchField
 */
public class TeleBatchFieldElement extends TeleInputElement {

    private TeleBatchElement parentBatch;

    /**
     * Batch field name
     */
    private String name;

    public TeleBatchFieldElement(TeleMethodElement parentTeleMethod, VarElement originElement, String name) {
        super(parentTeleMethod, originElement);
        this.name = name;
    }

    private void checkFieldName(String name) {
        //TODO
    }

    public String getterName() {
        return "get" + StrUtils.firstCharToUpperCase(name());
    }

    public String setterName() {
        return "set" + StrUtils.firstCharToUpperCase(name());
    }

    public TeleBatchElement parentBatch() {
        return parentBatch;
    }

    public void setParentBatch(TeleBatchElement parentBatch) {
        this.parentBatch = parentBatch;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
