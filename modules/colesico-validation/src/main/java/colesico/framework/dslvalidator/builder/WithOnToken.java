package colesico.framework.dslvalidator.builder;

import colesico.framework.dslvalidator.commands.ValueChain;

import java.util.function.Function;

public final class WithOnToken<T> implements CommandToken {

    private final ValidationProgramBuilder programBuilder;
    private final WithOnToken<T> parent;

    private final String subject;
    private final Function<T, Object> valueExtractor;
    private final CommandToken[] commands;

    public WithOnToken(ValidationProgramBuilder programBuilder) {
        this.programBuilder = programBuilder;
        this.parent = null;
        this.subject = null;
        this.valueExtractor = null;
        this.commands = null;
    }

    public WithOnToken(ValidationProgramBuilder programBuilder,
                       WithOnToken<T> parent,
                       String subject,
                       Function<T, Object> valueExtractor,
                       CommandToken... commands) {

        this.programBuilder = programBuilder;
        this.parent = parent;
        this.subject = subject;
        this.valueExtractor = valueExtractor;
        this.commands = commands;
    }

    /**
     * Commands executed until error is not occurred
     * @param subject
     * @param valueExtractor
     * @param commands
     * @return
     */
    public WithOnToken<T> on(final String subject,
                             final Function<T, Object> valueExtractor,
                             final CommandToken... commands) {

        return new WithOnToken<>(programBuilder, this, subject, valueExtractor, commands);
    }

    @Override
    public void build() {
        if (parent != null) {
            parent.build();
            programBuilder.begin(new ValueChain(subject, valueExtractor));
            for (CommandToken cmd : commands) {
                cmd.build();
            }
            programBuilder.end();
        }
    }
}
