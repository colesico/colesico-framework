package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;

import java.util.function.Predicate;

/**
 * Executes group commands if context value is not null.
 *
 * @see GroupSequence
 */
public class ConditionalGroup<V> extends AbstractSequence<V, V> {

    private final Predicate<ValidationContext> condition;

    public ConditionalGroup(Predicate<ValidationContext> condition) {
        this.condition = condition;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (condition.test(context)) {
            executeGroup(context);
        }
    }

}
