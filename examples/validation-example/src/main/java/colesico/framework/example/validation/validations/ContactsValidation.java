package colesico.framework.example.validation.validations;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.example.validation.dto.PostAddress;

public class ContactsValidation extends ContactsValidatorBuilder{

    public ContactsValidation(ValidatorMessages msg, CredentialsValidatorBuilder credentialsValidation) {
        super(msg, credentialsValidation);
    }

    @Override
    protected Command<PostAddress> validatePostAddress() {
        return null;
    }

    @Override
    protected void verifyPhone(ValidationContext<String> ctx) {

    }

    @Override
    protected Command<String> validateEmail() {
        return null;
    }
}
