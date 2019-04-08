package colesico.framework.dslvalidator.builder;

import colesico.framework.dslvalidator.Chain;
import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.commands.GroupChain;
import colesico.framework.dslvalidator.commands.ValueChain;

import java.util.function.Function;

public final class WithOnToken<T> {

    protected final WithOnToken<T> parent;
    private final Chain chain;

    public WithOnToken(WithOnToken<T> parent, Chain chain) {
        this.parent = parent;
        this.chain = chain;
    }

    /**
     * Commands executed until error is not occurred
     *
     * @param subject
     * @param valueExtractor
     * @param commands
     * @return
     */
    public WithOnToken<T> on(final String subject,
                             final Function<T, Object> valueExtractor,
                             final Command... commands) {

        return new WithOnToken<>(this, new ValueChain(subject, valueExtractor).addCommands(commands));
    }

    public Chain end() {
        if (parent == null) {
            return chain;
        } else {
            Chain parentChain = parent.end();
            parentChain.addCommands(chain);
            return parentChain;
        }
    }
}
