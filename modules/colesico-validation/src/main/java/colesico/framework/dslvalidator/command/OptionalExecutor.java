package colesico.framework.dslvalidator.command;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.Executor;
import colesico.framework.dslvalidator.ValidationContext;

/**
 * If context value is not null executes command within the current context.
 * otherwise dos nothing.
 */
public class OptionalExecutor<V> extends Executor<V> {

    public OptionalExecutor(Command<V> command) {
        super(command);
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (context.getValue() != null) {
            command.execute(context);
        }
    }
}
