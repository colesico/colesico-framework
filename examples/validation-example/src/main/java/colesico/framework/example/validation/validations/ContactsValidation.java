package colesico.framework.example.validation.validations;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.example.validation.dto.Contacts;
import colesico.framework.example.validation.dto.PostAddress;


abstract public class ContactsValidation extends AppValidatorBuilder<Contacts> {

    public ContactsValidation(ValidatorMessages msg) {
        super(msg);
    }

    protected Command<PostAddress> validatePostAddress() {
        return series(
                field("address", PostAddress::getAddress, required()),
                field("postCode", PostAddress::getPostCode, required(), length(10, 100))
        );
    }

    protected void verifyPhone(ValidationContext<String> ctx) {
        if (ctx.value() == null) {
            ctx.addError("PhoneError", "Invalid phone");
        }
    }

    protected Command<String> validateEmail() {
        return chain(
                required(),
                length(5, 200)
        );
    }
}
