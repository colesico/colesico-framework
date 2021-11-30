package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;

import java.util.function.Predicate;

/**
 * Executes chain commands if context value is not null.
 *
 * @see ChainSequence
 */
public class ConditionalChain<V> extends AbstractSequence<V, V> {

    private final Predicate<ValidationContext> condition;

    public ConditionalChain(Predicate<ValidationContext> condition) {
        this.condition = condition;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (condition.test(context)) {
            executeChain(context);
        }
    }

}
