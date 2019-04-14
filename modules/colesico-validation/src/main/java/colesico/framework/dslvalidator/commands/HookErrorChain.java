package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.translation.Translatable;

import java.util.List;

/**
 * This chain after commands excutions checks for nested errors presents.
 * If nested exceptions is present adds error to current context
 * @param
 */
public final class HookErrorChain extends AbstractChain {

    private final String errorCode;
    private final Translatable errorMessage;
    private final Object[] messageParams;

    public HookErrorChain(String errorCode, Translatable errorMessage, Object... messageParam) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.messageParams = messageParam;
    }

    protected boolean hasDeepErrors(ValidationContext context){
        if (context.hasErrors()){
            return true;
        }
        List<ValidationContext> childContexts = context.getChildContexts();
        for (ValidationContext childContext : childContexts) {
            if (childContext.hasErrors()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(ValidationContext context) {
        //ValidationContext childContext = ValidationContext.ofChild(context, context.getSubject(), context.getValue());
        executeCommands(context);
        if (hasDeepErrors(context)){
            context.addError(errorCode,errorMessage.translate(messageParams));
        }
    }
}
