package colesico.framework.dslvalidator.command;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;

import java.util.function.Predicate;

/**
 * Executes group commands if condition is met.
 *
 * @see GroupSequence
 */
public class ConditionalGroup<V> extends AbstractSequence<V, V> {

    private final Predicate<ValidationContext> condition;

    public ConditionalGroup(Predicate<ValidationContext> condition) {
        this.condition = condition;
    }

    public ConditionalGroup(Predicate<ValidationContext> condition, Command<V>[] commands) {
        super(commands);
        this.condition = condition;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (condition.test(context)) {
            executeGroup(context);
        }
    }

}
