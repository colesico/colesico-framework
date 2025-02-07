package colesico.framework.dslvalidator.command;

import colesico.framework.dslvalidator.Iterator;
import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;

/**
 * Executes all sequence commands within the current context
 * until first validation error occurs.
 */
public class ChainIterator<V> extends Iterator<V> {

    public ChainIterator() {
    }

    public ChainIterator(Command<V>[] commands) {
        super(commands);
    }

    /**
     * Execute sequence commands until error occurred
     */
    @Override
    public void execute(ValidationContext<V> context) {
        if (commands.isEmpty()) {
            throw new RuntimeException(this.getClass().getSimpleName()+": validation commands collection is empty. Context:" + context);
        }
        for (Command<V> command : commands) {
            command.execute(context);
            if (context.hasErrors()) {
                return;
            }
        }
    }
}
