package colesico.framework.dslvalidator;

/**
 * Command executor.
 * Performs the command execution.
 */
abstract public class Executor<V> implements Command<V> {

    protected final Command<V> command;

    public Executor(Command<V> command) {
        if (command == null) {
            throw new RuntimeException(this.getClass().getSimpleName() + ": validation command is null");
        }
        this.command = command;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "command=" + command +
                '}';
    }

}
