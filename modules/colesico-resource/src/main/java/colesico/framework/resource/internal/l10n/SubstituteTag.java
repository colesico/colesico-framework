package colesico.framework.resource.internal.l10n;

public record SubstituteTag(int startPosition, int endPosition, String substitution) implements PathTag {
    @Override
    public String toString() {
        return "SubstituteTag{" +
                "startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", substitution='" + substitution + '\'' +
                '}';
    }
}
