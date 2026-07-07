package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StringUtils;
import colesico.framework.service.codegen.model.ServiceParameterElement;

/**
 * Represents field of batch class and corresponding method parameter
 *
 * @see colesico.framework.service.BatchField
 */
public class TeleBatchParamElement extends TeleParameterElement {

    /**
     * Parent batch elm reference
     */
    private TeleBatchElement parentBatch;

    /**
     * Batch field name
     */
    private String name;

    public TeleBatchParamElement(TeleCommandElement parentTeleCommand, ServiceParameterElement serviceParam, String name) {
        super(parentTeleCommand, serviceParam);
        this.name = name;
    }

    private void checkFieldName(String name) {
        //TODO
    }

    public String getterName() {
        return "get" + StringUtils.firstCharToUpperCase(name());
    }

    public String setterName() {
        return "set" + StringUtils.firstCharToUpperCase(name());
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
