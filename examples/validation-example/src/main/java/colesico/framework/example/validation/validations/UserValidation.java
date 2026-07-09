package colesico.framework.example.validation.validations;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.example.validation.dto.Credentials;
import colesico.framework.example.validation.dto.User;

abstract public class UserValidation extends AppValidatorBuilder<User> {


    public UserValidation(ValidatorMessages msg) {
        super(msg);
    }

    protected Command<Long> validateId() {
        return chain(
                required(),
                positive()
        );
    }

    protected Command<String> validateName() {
        return required();
    }
}
