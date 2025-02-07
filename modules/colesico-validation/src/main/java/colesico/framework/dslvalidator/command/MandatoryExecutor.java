package colesico.framework.dslvalidator.command;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.Executor;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

/**
 * If context value is not null executes command within the current context.
 * If current context value is null throws validation exception.
 */
public class MandatoryExecutor<V> extends Executor<V> {
    private final ValidatorMessages msg;

    public MandatoryExecutor(ValidatorMessages msg, Command<V> command) {
        super(command);
        this.msg = msg;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (context.getValue() != null) {
            command.execute(context);
        } else {
            context.addError(MandatoryExecutor.class.getSimpleName(), msg.mandatoryValueIsNull());
        }
    }
}
