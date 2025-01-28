package colesico.framework.resource.internal.l10n;

public record SubstituteTag(int fromPosition, int toPosition, String substitution) implements PathTag {
    @Override
    public String toString() {
        return "SubstituteTag{" +
                "fromPosition=" + fromPosition +
                ", toPosition=" + toPosition +
                ", substitution='" + substitution + '\'' +
                '}';
    }
}
