package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;

/**
 * Assumes that the current context value is iterable.
 * Applies commands to each iterated value within own sub context
 *
 * @param <V>
 */
public class IterableSequence<V extends Iterable<I>, I> extends AbstractSequence<V, I> {

    @Override
    public void execute(ValidationContext<V> context) {
        Iterable<I> items = context.getValue();
        if (items == null) {
            return;
        }
        int idx = 0;
        for (I itemValue : items) {
            ValidationContext<I> nestedContext = ValidationContext.ofNested(context, Integer.toString(idx++), itemValue);
            executeChain(nestedContext);
        }
    }

}
