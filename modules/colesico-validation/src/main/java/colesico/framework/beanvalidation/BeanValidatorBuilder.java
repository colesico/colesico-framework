package colesico.framework.beanvalidation;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.DSLValidator;
import colesico.framework.dslvalidator.builder.AbstractValidatorBuilder;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

abstract public class BeanValidatorBuilder<V> extends AbstractValidatorBuilder {

    public static final String VALIDATION_METHOD = "validation";

    public BeanValidatorBuilder(ValidatorMessages msg) {
        super(msg);
    }

    /**
     * Root validation subject
     */
    public String subject() {
        return null;
    }

    /**
     * Returns bean validation flow.
     */
    abstract public Command<V> validation();

    /**
     * Builds bean validator
     */
    @SuppressWarnings("unchecked")
    public DSLValidator<V> build() {
        return validator(subject(), validation());
    }

}
