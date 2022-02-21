package colesico.framework.service.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent batch parameter
 * @see colesico.framework.service.BatchField
 */
public class TeleBatchElement {

    /**
     * Parent tele-method ref
     */
    protected final TeleMethodElement parentTeleMethod;

    /**
     * Batch name
     */
    protected final String name;

    protected final List<TeleBatchFieldElement> fields = new ArrayList<>();

    protected TRContextElement readingContext;

    public TeleBatchElement(TeleMethodElement parentTeleMethod, String name) {
        this.parentTeleMethod = parentTeleMethod;
        this.name = name;
    }

    public void addField(TeleBatchFieldElement field) {
        fields.add(field);
        field.setParentBatch(this);
    }

    public TeleMethodElement getParentTeleMethod() {
        return parentTeleMethod;
    }

    public String getName() {
        return name;
    }

    public List<TeleBatchFieldElement> getFields() {
        return fields;
    }

    public TRContextElement getReadingContext() {
        return readingContext;
    }

    public void setReadingContext(TRContextElement readingContext) {
        this.readingContext = readingContext;
    }
}
