package colesico.framework.config.codegen;

import colesico.framework.assist.codegen.model.FieldElement;

public class SourceValueElement {
    private final FieldElement originField;
    private final String query;

    public SourceValueElement(FieldElement originField, String query) {
        this.originField = originField;
        this.query = query;
    }

    public FieldElement getOriginField() {
        return originField;
    }

    public String getQuery() {
        return query;
    }
}
