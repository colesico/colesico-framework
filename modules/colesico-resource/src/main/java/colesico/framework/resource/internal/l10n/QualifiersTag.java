package colesico.framework.resource.internal.l10n;

public record QualifiersTag(int position) implements Tag {
    @Override
    public String toString() {
        return "QualifiersTag{" +
                "position=" + position +
                '}';
    }
}
