package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

/**
 * If context value is not null executes all sequence commands within the current context.
 * Commands executed until first validation error occurs.
 * <p>
 * If current context value is null throws validation exception.
 */
public class MandatoryChain<V> extends AbstractSequence<V, V> {
    private final ValidatorMessages msg;

    public MandatoryChain(ValidatorMessages msg) {
        this.msg = msg;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (context.getValue() != null) {
            executeChain(context);
        } else {
            context.addError(MandatoryChain.class.getSimpleName(), msg.mandatoryValueIsNull());
        }
    }
}
