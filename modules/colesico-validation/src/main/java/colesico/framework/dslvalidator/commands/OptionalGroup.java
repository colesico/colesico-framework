package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;

/**
 * Executes group commands if context value is not null.
 *
 * @see GroupSequence
 */
public class OptionalGroup<V> extends AbstractSequence<V, V> {

    @Override
    public void execute(ValidationContext<V> context) {
        if (context.getValue() != null) {
            executeGroup(context);
        }
    }

}
