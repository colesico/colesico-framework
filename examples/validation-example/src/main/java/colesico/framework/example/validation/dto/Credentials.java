package colesico.framework.example.validation.dto;

import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.ValidatorBuilderPrototype;
import colesico.framework.example.validation.validations.AppValidatorBuilder;

@ValidatorBuilderPrototype(superclass = AppValidatorBuilder.class)
@ValidatorBuilderPrototype(name = "brief", superclass = AppValidatorBuilder.class)
public class Credentials {

    @Validate
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
