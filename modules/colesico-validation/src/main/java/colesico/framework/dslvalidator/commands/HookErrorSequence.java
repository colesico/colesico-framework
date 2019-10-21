package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.translation.Translatable;

import java.util.List;

/**
 * This sequence after commands execution checks for nested errors presents.
 * If nested exceptions is present adds error to current context
 *
 * @param
 */
public final class HookErrorSequence<V> extends AbstractSequence<V, V> {

    private final String errorCode;
    private final Translatable errorMessage;
    private final Object[] messageParams;

    public HookErrorSequence(String errorCode, Translatable errorMessage, Object... messageParam) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.messageParams = messageParam;
    }

    protected boolean hasDeepErrors(ValidationContext<V> context) {
        if (context.hasErrors()) {
            return true;
        }
        List<ValidationContext<?>> nestedContexts = context.getNestedContexts();
        for (ValidationContext nestedCtx : nestedContexts) {
            if (nestedCtx.hasErrors()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        executeChain(context);
        if (hasDeepErrors(context)) {
            context.addError(errorCode, errorMessage.translate(messageParams));
        }
    }
}
