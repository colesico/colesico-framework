package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.Iterator;

/**
 * Represents tele-method entry: parameter, compound, batch field
 */
abstract public class TeleEntryElement extends TeleMethodRelatedElement {

    /**
     * Optional parent compound
     */
    private TeleCompoundElement parentCompound;

    public TeleEntryElement(TeleMethodElement parentTeleMethod, VarElement originElement) {
        super(parentTeleMethod, originElement);
    }

    /**
     * To iterate from current node to root compound
     */
    public Iterator<TeleEntryElement> getIterator() {
        return new CompoundNodeIterator(this);
    }

    public TeleCompoundElement getParentCompound() {
        return parentCompound;
    }

    public void setParentCompound(TeleCompoundElement parentCompound) {
        this.parentCompound = parentCompound;
    }

    public static class CompoundNodeIterator implements Iterator<TeleEntryElement> {

        private TeleEntryElement currentCompound;

        public CompoundNodeIterator(TeleEntryElement currentArgument) {
            this.currentCompound = currentArgument;
        }

        @Override
        public boolean hasNext() {
            return currentCompound != null;
        }

        @Override
        public TeleEntryElement next() {
            TeleEntryElement prev = currentCompound;
            if (currentCompound != null) {
                currentCompound = currentCompound.getParentCompound();
            }
            return prev;
        }
    }
}
