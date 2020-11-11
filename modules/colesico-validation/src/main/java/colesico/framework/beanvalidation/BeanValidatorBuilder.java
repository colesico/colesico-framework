package colesico.framework.beanvalidation;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.DSLValidator;
import colesico.framework.dslvalidator.builder.ValidatorBuilder;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

abstract public class BeanValidatorBuilder extends ValidatorBuilder {

    public static final String COMMANDS_METHOD = "commands";

    public BeanValidatorBuilder(ValidatorMessages vrMessages) {
        super(vrMessages);
    }

    /**
     * Returns bean properties validation commands.
     */
    abstract public Command[] commands();

    /**
     * Builds bean validator
     */
    public <V> DSLValidator<V> build() {
        return validator(mandatoryGroup(commands()));
    }

}
