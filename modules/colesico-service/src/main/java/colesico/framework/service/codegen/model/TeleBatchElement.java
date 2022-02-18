package colesico.framework.service.codegen.model;

import java.util.ArrayList;
import java.util.List;

public class TeleBatchElement {

    /**
     * Parent tele-method ref
     */
    protected final TeleMethodElement parentTeleMethod;

    protected final List<TeleBatchFieldElement> fields = new ArrayList<>();

    protected TRContextElement readingContext;

    public TeleBatchElement(TeleMethodElement parentTeleMethod) {
        this.parentTeleMethod = parentTeleMethod;
    }

    public void addField(TeleBatchFieldElement field) {
        fields.add(field);
        field.setParentBatch(this);
    }

    public TeleMethodElement getParentTeleMethod() {
        return parentTeleMethod;
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
