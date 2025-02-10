package colesico.framework.example.validation.validations;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

public class UserValidation extends UserValidatorBuilder {

    public UserValidation(ValidatorMessages msg, CredentialsValidatorBuilder credentialsValidation, ContactsValidatorBuilder contactsValidation) {
        super(msg, credentialsValidation, contactsValidation);
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
