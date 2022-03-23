package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents tele compound
 *
 * @see colesico.framework.service.Compound
 */
public class TeleCompoundElement extends TeleEntryElement {

    /**
     * Compound fields
     */
    private final List<TeleEntryElement> fields = new ArrayList<>();

    public TeleCompoundElement(TeleMethodElement parentTeleMethod, VarElement originElement) {
        super(parentTeleMethod, originElement);
    }

    public void addField(TeleEntryElement field) {
        fields.add(field);
        field.setParentCompound(this);
    }

    public List<TeleEntryElement> getFields() {
        return fields;
    }
}
