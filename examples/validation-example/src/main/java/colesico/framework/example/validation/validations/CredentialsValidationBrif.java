package colesico.framework.example.validation.validations;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.example.validation.dto.Credentials;

abstract public class CredentialsValidationBrif extends AppValidatorBuilder<Credentials> {

    public CredentialsValidationBrif(ValidatorMessages msg) {
        super(msg);
    }

    protected Command<String> validatePassword() {
        return required();
    }
}
