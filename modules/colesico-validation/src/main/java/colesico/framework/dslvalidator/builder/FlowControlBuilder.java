package colesico.framework.dslvalidator.builder;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.DSLValidator;
import colesico.framework.dslvalidator.commands.*;
import colesico.framework.translation.Translatable;

import java.util.function.Function;

abstract public class FlowControlBuilder {
    /**
     * Defines validation algorithm
     *
     * @param commands
     * @return
     */
    protected final <V> DSLValidator<V> program(final Command... commands) {
        Command program = new GroupChain().addCommands(commands);
        return new DSLValidator<>(program, null);
    }

    protected final <V> DSLValidator<V> program(final String subject, final Command... commands) {
        Command program = new SerialChain();
        return new DSLValidator<>(program, subject);
    }

    /**
     * Executes commands within the local context.
     * In case of local validation errors occur, command execution is NOT interrupted.
     *
     * @param commands
     * @return
     * @see GroupChain
     */
    protected final Command group(final Command... commands) {
        return new GroupChain().addCommands(commands);
    }

    /**
     * Executes commands within the local context.
     * In case of local validation errors occur, command execution is interrupted.
     * @param commands
     * @return
     */
    protected final Command serial(final Command... commands) {
        return new SerialChain().addCommands(commands);
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
    protected final Command on(final String subject, final Command... commands) {
        return new SubjectChain(subject).addCommands(commands);
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
    protected final <T> Command on(final String subject, final Function<T, Object> valueExtractor, final Command... commands) {
        return new ValueChain(subject, valueExtractor).addCommands(commands);
    }

    /**
     * In case of local  validation errors occur, command execution is interrupted.
     *
     * @param subject
     * @param index
     * @param commands
     * @return
     */
    protected final Command on(final String subject, final int index, final Command... commands) {
        return new ElementChain(subject, index).addCommands(commands);
    }

    /**
     * Iterates the elements of value from local context.
     * In case of  local validation errors occur, command execution is interrupted.
     *
     * @param
     * @return
     * @see ForEachChain
     */
    protected final Command forEach(final Command... commands) {
        return new ForEachChain().addCommands(commands);
    }

    protected final Command ifOk(final Command... commands) {
        return new IfOkChain().addCommands(commands);
    }

    protected final Command ifNotNull(final Command... commands) {
        return new IfNotNullChain().addCommands(commands);
    }

    protected final Command hookError(String errorCode, Translatable errorMsg, final Command... commands){
        return new HookErrorChain(errorCode,errorMsg).addCommands(commands);
    }

    protected final <T> WithOnToken<T> with(Class<T> classis) {
        return new WithOnToken<>(null, new GroupChain());
    }

}
