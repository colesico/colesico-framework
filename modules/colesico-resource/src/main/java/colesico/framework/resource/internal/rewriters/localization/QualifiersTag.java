package colesico.framework.resource.internal.rewriters.localization;

public record QualifiersTag(int position) implements PathTag {
    @Override
    public String toString() {
        return "QualifiersTag{" +
                "position=" + position +
                '}';
    }
}
