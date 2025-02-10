package colesico.framework.example.validation.validations;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

public class CredentialsValidation extends CredentialsValidatorBuilder {
    public CredentialsValidation(ValidatorMessages msg) {
        super(msg);
    }

    @Override
    protected Command<String> validatePassword() {
        return chain(
                required(),
                length(5, 32)
        );
    }
}
