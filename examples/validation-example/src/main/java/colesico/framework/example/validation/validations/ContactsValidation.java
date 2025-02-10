package colesico.framework.example.validation.validations;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.example.validation.dto.PostAddress;

import static colesico.framework.example.validation.validations.PostAddressValidatorBuilder.ADDRESS;
import static colesico.framework.example.validation.validations.PostAddressValidatorBuilder.POST_CODE;

public class ContactsValidation extends ContactsValidatorBuilder {

    public ContactsValidation(ValidatorMessages msg, CredentialsBriefValidatorBuilder credentialsValidation) {
        super(msg, credentialsValidation);
    }

    @Override
    protected Command<PostAddress> validatePostAddress() {
        return series(
                field(ADDRESS, required()),
                field(POST_CODE, required(), length(10, 100))
        );
    }

    @Override
    protected void verifyPhone(ValidationContext<String> ctx) {
        if (ctx.getValue() == null) {
            ctx.addError("PhoneError", "Invalid phone");
        }
    }

    @Override
    protected Command<String> validateEmail() {
        return chain(
                required(),
                length(5, 200)
        );
    }
}
