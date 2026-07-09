package colesico.framework.example.validation.validations;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.example.validation.dto.PostAddress;

abstract public class PostAddressValidatorBuilder extends AppValidatorBuilder<PostAddress> {
    public PostAddressValidatorBuilder(ValidatorMessages msg) {
        super(msg);
    }

    /**
     * Validate postCode
     */
    protected Command<String> validatePostCode() {
        return null;
    }

    /**
     * Validate address
     */
    protected Command<String> validateAddress() {
        return null;
    }

}
