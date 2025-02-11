package colesico.framework.example.validation.validations;

import colesico.framework.beanvalidation.ValidatorBuilder;
import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

@ValidatorBuilder
public class UserValidation extends AbstractUserValidatorBuilder {

    public UserValidation(ValidatorMessages msg,
                          AbstractCredentialsValidatorBuilder credentialsVB,
                          AbstractContactsValidatorBuilder contactsVB) {
        super(msg, credentialsVB, contactsVB);
    }

    @Override
    protected Command<Long> validateId() {
        return chain(
                required(),
                positive()
        );
    }

    @Override
    protected Command<String> validateName() {
        return required();
    }
}
