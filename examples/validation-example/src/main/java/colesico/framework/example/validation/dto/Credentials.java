package colesico.framework.example.validation.dto;

import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.ValidatorBuilder;
import colesico.framework.example.validation.validations.CredentialsBriefValidation;
import colesico.framework.example.validation.validations.CredentialsValidation;

@ValidatorBuilder(CredentialsValidation.class)
@ValidatorBuilder(value = CredentialsBriefValidation.class, name = "brief")
public class Credentials {

    @Validate(builders = {"default", "brief"})
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
