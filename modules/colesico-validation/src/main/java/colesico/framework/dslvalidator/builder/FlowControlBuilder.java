package colesico.framework.dslvalidator.builder;

import colesico.framework.dslvalidator.commands.*;

import java.util.ArrayDeque;
import java.util.function.Function;

abstract public class FlowControlBuilder extends ValidationProgramBuilder {

    /**
     * Defines validation algorithm
     *
     * @param commands
     * @return
     */
    protected final ProgramCompileToken program(final CommandToken... commands) {
        setStack(new ArrayDeque<>());
        begin(new GroupChain());
        for (CommandToken cmd : commands) {
            cmd.build();
        }
        return new ProgramCompileToken(this, null);
    }

    protected final ProgramCompileToken program(final String subject, final CommandToken... commands) {
        setStack(new ArrayDeque<>());
        begin(new SerialChain());
        for (CommandToken cmd : commands) {
            cmd.build();
        }
        return new ProgramCompileToken(this, subject);
    }


    /**
     * Executes commands within the local context.
     * In case of local validation errors occur, command execution is NOT interrupted.
     *
     * @param commands
     * @return
     * @see GroupChain
     */
    protected final CommandToken group(final CommandToken... commands) {
        return () -> {
            begin(new GroupChain());
            for (CommandToken cmd : commands) {
                cmd.build();
            }
            end();
        };
    }

    /**
     * Executes commands within the new child context with specified subject.
     * In case of local validation errors occur, commands execution is interrupted.
     *
     * @param subject
     * @param commands
     * @return
     * @see SubjectChain
     */
    protected final CommandToken on(final String subject, final CommandToken... commands) {
        return () -> {
            begin(new SubjectChain(subject));
            for (CommandToken cmd : commands) {
                cmd.build();
            }
            end();
        };
    }

    /**
     * Creates new child context with the subject and the value, extracted from the value of the local context.
     * Execute commands within that child context.
     * In case of validation errors occur in the child context, command execution is interrupted.
     *
     * @param subject
     * @param valueExtractor
     * @param commands
     * @param <T>
     * @return
     * @see ValueChain
     */
    protected final <T> CommandToken on(final String subject, final Function<T, Object> valueExtractor, final CommandToken... commands) {
        return () -> {
            begin(new ValueChain(subject, valueExtractor));
            for (CommandToken cmd : commands) {
                cmd.build();
            }
            end();
        };
    }

    /**
     * In case of local  validation errors occur, command execution is interrupted.
     *
     * @param subject
     * @param index
     * @param commands
     * @return
     */
    protected final CommandToken on(final String subject, final int index, final CommandToken... commands) {
        return () -> {
            begin(new ElementChain(subject, index));
            for (CommandToken cmd : commands) {
                cmd.build();
            }
            end();
        };
    }

    /**
     * Iterates the elements of value from local context.
     * In case of  local validation errors occur, command execution is interrupted.
     *
     * @param
     * @return
     * @see ForEachChain
     */
    protected final CommandToken forEach(final CommandToken... commands) {
        return () -> {
            begin(new ForEachChain());
            for (CommandToken cmd : commands) {
                cmd.build();
            }
            end();
        };
    }

    protected final CommandToken ifOk(final CommandToken... commands) {
        return () -> {
            begin(new IfOkChain());
            for (CommandToken cmd : commands) {
                cmd.build();
            }
            end();
        };
    }

    /**
     * Executes commands within the current context.
     *
     * @param commands
     * @return
     */
    protected final CommandToken inline(final CommandToken... commands) {
        return () -> {
            for (CommandToken cmd : commands) {
                cmd.build();
            }
        };
    }

    protected final <T> WithOnToken<T> with(Class<T> clazz) {
        return new WithOnToken<>(this);
    }

}
