package colesico.framework.dslvalidator.command;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.Executor;
import colesico.framework.dslvalidator.ValidationContext;

import java.util.function.Predicate;

/**
 * Executes command under current context if condition is met.
 *
 * @see ChainIterator
 */
public class ConditionalExecutor<V> extends Executor<V> {

    private final Predicate<ValidationContext<V>> condition;

    public ConditionalExecutor(Predicate<ValidationContext<V>> condition, Command<V> command) {
        super(command);
        this.condition = condition;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (condition.test(context)) {
            command.execute(context);
        }
    }

}
