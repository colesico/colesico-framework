package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.FieldElement;

public class TeleAggregateFieldElement implements TeleReadableElement {

    private final FieldElement originField;

    private TeleReadElement readSpec;

    public TeleAggregateFieldElement(FieldElement originField) {
        this.originField = originField;
    }

    public FieldElement originField() {
        return originField;
    }

    @Override
    public TeleReadElement readSpec() {
        return readSpec;
    }

    @Override
    public void setReadSpec(TeleReadElement readSpec) {
        this.readSpec = readSpec;
    }
}
