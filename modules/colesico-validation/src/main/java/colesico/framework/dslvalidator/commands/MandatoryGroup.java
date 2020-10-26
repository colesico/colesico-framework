package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

/**
 * If context value is not null executes all sequence commands within the current context.
 * Commands execution is not interrupted when validation errors occur.
 * <p>
 * If current context value is null throws validation exception.
 */
public class MandatoryGroup<V> extends AbstractSequence<V, V> {
    private final ValidatorMessages msg;

    public MandatoryGroup(ValidatorMessages msg) {
        this.msg = msg;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (context.getValue() != null) {
            executeGroup(context);
        } else {
            context.addError(MandatoryGroup.class.getSimpleName(), msg.mandatoryValueIsNull());
        }
    }
}
