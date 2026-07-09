package colesico.framework.example.validation.dto;

import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.ValidatorBuilder;
import colesico.framework.example.validation.validations.CredentialsValidationBrif;
import colesico.framework.example.validation.validations.CredentialsValidation;

@ValidatorBuilder(CredentialsValidation.class)
@ValidatorBuilder(superclass = CredentialsValidationBrif.class, isDefault = false)
public class Credentials {

    @Validate(CredentialsValidation.class)
    @Validate(CredentialsValidationBrif.class)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
