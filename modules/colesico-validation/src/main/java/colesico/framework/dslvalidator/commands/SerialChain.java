package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;

/**
 * Executes chain commands until first validation error occur
 */
public class SerialChain extends AbstractChain {
    @Override
    public void execute(ValidationContext context) {
        executeCommands(context);
    }
}
