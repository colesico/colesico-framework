package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;

public class ForEachChain<V extends Iterable> extends AbstractChain<V> {

    @Override
    public void execute(ValidationContext<V> context) {
        Iterable values = context.getValue();
        if (values==null){
            return;
        }
        int index = 0;
        for (Object val : values) {
            ValidationContext childContext = ValidationContext.ofChild(context, context.getSubject() + '[' + (index++) + ']', val);
            executeCommands(childContext);
        }
    }
}
