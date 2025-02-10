package colesico.framework.dslvalidator.command;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

public class PositiveVerifier<V extends Number> implements Command<V> {

    protected final ValidatorMessages msg;

    public PositiveVerifier(ValidatorMessages msg) {
        this.msg = msg;
    }

    protected void addError(ValidationContext<V> context) {
        context.addError(this.getClass().getSimpleName(), msg.positiveValueRequired());
    }

    @Override
    public void execute(ValidationContext<V> context) {
        Number value = context.getValue();
        if (value != null) {
            switch (value) {
                case Long val -> {
                    if (val < 0) {
                        addError(context);
                    }
                }
                case Integer val -> {
                    if (val < 0) {
                        addError(context);
                    }
                }
                case Double val -> {
                    if (val < 0) {
                        addError(context);
                    }
                }
                case Byte val -> {
                    if (val < 0) {
                        addError(context);
                    }
                }
                case Short val -> {
                    if (val < 0) {
                        addError(context);
                    }
                }
                case Float val -> {
                    if (val < 0) {
                        addError(context);
                    }
                }
                default -> {
                    throw new RuntimeException("Unsupported value type: " + value);
                }
            }
        }
    }
}
