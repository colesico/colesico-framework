package colesico.framework.dslvalidator.builder;

import colesico.framework.dslvalidator.Chain;
import colesico.framework.dslvalidator.Command;

import java.util.Deque;

public class ValidationProgramBuilder {

    private Deque<Chain> stack;

    public Deque<Chain> getStack() {
        return stack;
    }

    public void setStack(Deque<Chain> stack) {
        this.stack = stack;
    }

    /**
     * Starts new nested chain.
     *
     * @param chain
     * @return
     */
    public final void begin(final Chain chain) {
        Chain parentChain = stack.peek();
        stack.push(chain);
        if (parentChain != null) {
            parentChain.getCommands().add(chain);
        }
    }

    /**
     * Ends current chain and returns to the parent one.
     */
    public final void end() {
        stack.pop();
        if (stack.isEmpty()) {
            throw new RuntimeException("Validation tree is empty");
        }
    }

    /**
     * Adds command to current chain
     *
     * @return
     */
    public final void add(Command command) {
        stack.peek().getCommands().add(command);
    }
}
