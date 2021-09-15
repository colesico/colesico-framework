package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TeleCompoundElement extends TeleArgumentElement {

    private final List<TeleArgumentElement> fields = new ArrayList<>();

    public TeleCompoundElement(VarElement originVariable) {
        super(originVariable);
    }

    /**
     * To iterate from current compound to root compound
     */
    public Iterator<TeleCompoundElement> getIterator() {
        return new CompoundIterator(this);
    }

    public void addField(TeleArgumentElement field) {
        fields.add(field);
    }

    public List<TeleArgumentElement> getFields() {
        return fields;
    }

    static class CompoundIterator implements Iterator<TeleCompoundElement> {

        private TeleCompoundElement currentCompound;

        public CompoundIterator(TeleCompoundElement currentCompound) {
            this.currentCompound = currentCompound;
        }

        @Override
        public boolean hasNext() {
            return currentCompound != null;
        }

        @Override
        public TeleCompoundElement next() {
            TeleCompoundElement prev = currentCompound;
            if (currentCompound != null) {
                currentCompound = currentCompound.getParentCompound();
            }
            return prev;
        }
    }


}
