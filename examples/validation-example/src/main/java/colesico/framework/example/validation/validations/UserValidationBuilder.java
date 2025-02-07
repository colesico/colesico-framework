package colesico.framework.example.validation.validations;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

public class UserValidationBuilder extends UserValidation{

    public UserValidationBuilder(ValidatorMessages msg, CredentialsValidation credentialsValidation, ContactsValidation contactsValidation) {
        super(msg, credentialsValidation, contactsValidation);
    }

    @Override
    protected Command<Long> validateId() {
        return null;
    }

    @Override
    protected Command<String> validateName() {
        return null;
    }
}
