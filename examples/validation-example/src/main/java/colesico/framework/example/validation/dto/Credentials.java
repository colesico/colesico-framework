package colesico.framework.example.validation.dto;

import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.ValidatorBuilder;
import colesico.framework.example.validation.validations.AppValidatorBuilder;

@ValidatorBuilder(superclass = AppValidatorBuilder.class)
public class Credentials {

    @Validate
    private String  password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
