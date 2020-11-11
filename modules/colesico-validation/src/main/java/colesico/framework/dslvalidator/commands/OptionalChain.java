package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;

/**
 * Executes chain commands if context value is not null.
 *
 * @see ChainSequence
 */
public class OptionalChain<V> extends AbstractSequence<V, V> {

    @Override
    public void execute(ValidationContext<V> context) {
        if (context.getValue() != null) {
            executeChain(context);
        }
    }

}
