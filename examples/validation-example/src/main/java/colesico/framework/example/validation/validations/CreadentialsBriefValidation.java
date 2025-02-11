package colesico.framework.example.validation.validations;

import colesico.framework.beanvalidation.ValidatorBuilder;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

@ValidatorBuilder
public class CreadentialsBriefValidation extends CredentialsBriefValidatorBuilder{
    public CreadentialsBriefValidation(ValidatorMessages msg) {
        super(msg);
    }
}
