package colesico.framework.beanvalidation;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.DSLValidator;
import colesico.framework.dslvalidator.builder.AbstractValidatorBuilder;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

abstract public class BeanValidatorBuilder<V> extends AbstractValidatorBuilder {

    public static final String COMMANDS_METHOD = "commands";

    public BeanValidatorBuilder(ValidatorMessages vrMessages) {
        super(vrMessages);
    }

    /**
     * Returns bean properties validation commands.
     */
    abstract public Command<V>[] commands();

    /**
     * Builds bean validator
     */
    @SuppressWarnings("unchecked")
    public DSLValidator<V> build() {
        return validator(commands());
    }

}
