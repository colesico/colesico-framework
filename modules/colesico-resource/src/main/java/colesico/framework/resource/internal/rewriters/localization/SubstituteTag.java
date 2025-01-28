package colesico.framework.resource.internal.rewriters.localization;

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
