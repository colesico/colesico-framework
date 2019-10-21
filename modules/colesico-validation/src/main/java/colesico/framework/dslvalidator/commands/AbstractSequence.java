package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.Sequence;
import colesico.framework.dslvalidator.ValidationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Sequence basis
 * @param <V>
 * @param <C>
 */
abstract public class AbstractSequence<V, C> implements Sequence<V, C> {

    protected final ArrayList<Command<C>> commands = new ArrayList<>();

    @Override
    public List<Command<C>> getCommands() {
        return commands;
    }

    /**
     * Execute sequence commands until error occurred
     *
     * @param context
     */
    protected void executeChain(ValidationContext<C> context) {
        for (Command<C> command : commands) {
            if (context.hasErrors()) {
                return;
            }
            command.execute(context);
        }
    }

    /**
     * Executes all sequence commands
     *
     * @param context
     */
    protected void executeGroup(ValidationContext<C> context) {
        for (Command<C> command : commands) {
            command.execute(context);
        }
    }

}
