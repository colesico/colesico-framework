package colesico.framework.example.validation.validations;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.example.validation.dto.Credentials;

abstract public class CredentialsValidation extends AppValidatorBuilder<Credentials> {

    public CredentialsValidation(ValidatorMessages msg) {
        super(msg);
    }

    protected Command<String> validatePassword() {
        return chain(
                required(),
                length(5, 32)
        );
    }

    protected Command<String> validatePasswordBrief() {
        return required();
    }
}
