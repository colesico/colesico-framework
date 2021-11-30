package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;

import java.util.function.Predicate;

/**
 * Executes group commands if context value is not null.
 *
 * @see GroupSequence
 */
public class OptionalGroup<V> extends AbstractSequence<V, V> {

    private final Predicate<ValidationContext> condition;

    public OptionalGroup(Predicate<ValidationContext> condition) {
        this.condition = condition;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (condition.test(context)) {
            executeGroup(context);
        }
    }

}
