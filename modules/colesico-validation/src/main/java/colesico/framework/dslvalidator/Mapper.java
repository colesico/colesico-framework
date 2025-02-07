package colesico.framework.dslvalidator;

/**
 * Map current context value to new nested context value, then execute command within that nested context
 *
 * @param <V> type of value to which to be mapped to N (type of values in current context)
 * @param <N> type of mapped  value  (extracted from value) to which the command applies
 */
abstract public class Mapper<V, N> implements Command<V> {

    /**
     * Nested context subject
     */
    protected final String subject;

    protected final Command<N> command;

    public Mapper(String subject, Command<N> command) {
        this.subject = subject;
        if (command == null) {
            throw new RuntimeException(this.getClass().getSimpleName() + ": validation command is null");
        }
        this.command = command;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "subject='" + subject + '\'' +
                ", command=" + command +
                '}';
    }
}
