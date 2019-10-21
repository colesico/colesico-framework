package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.Sequence;
import colesico.framework.dslvalidator.ValidationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Executes sequence commands  within the current context
 * until first validation error occurs
 *
 * @see GroupSequence
 */
public final class ChainSequence<V> extends AbstractSequence<V, V> {

    @Override
    public void execute(ValidationContext<V> context) {
       executeChain(context);
    }
}
