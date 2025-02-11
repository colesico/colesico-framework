package colesico.framework.example.validation.validations;

import colesico.framework.beanvalidation.ValidatorBuilder;
import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

@ValidatorBuilder
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
