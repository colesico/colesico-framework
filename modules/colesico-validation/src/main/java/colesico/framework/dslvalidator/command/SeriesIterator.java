package colesico.framework.dslvalidator.command;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.Iterator;
import colesico.framework.dslvalidator.ValidationContext;

/**
 * Successively executes all commands within the current context.
 * Commands execution is not interrupted when validation errors occur.
 */
public class SeriesIterator<V> extends Iterator<V> {

    public SeriesIterator() {
    }

    public SeriesIterator(Command<V>[] commands) {
        super(commands);
    }

    /**
     * Executes all sequence commands
     */
    @Override
    public void execute(ValidationContext<V> context) {
        if (commands.isEmpty()) {
            throw new RuntimeException(this.getClass().getSimpleName() + ": validation commands collection is empty. Context:" + context);
        }
        for (Command<V> command : commands) {
            command.execute(context);
        }
    }
}
