package colesico.framework.example.validation.validations;

import colesico.framework.beanvalidation.BeanValidatorBuilder;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

abstract public class AppValidatorBuilder<V> extends BeanValidatorBuilder<V> {

    public AppValidatorBuilder(ValidatorMessages msg) {
        super(msg);
    }
}
