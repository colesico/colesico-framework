package colesico.framework.example.validation.dto;

import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.BeanValidate;
import colesico.framework.beanvalidation.ValidatorBuilder;
import colesico.framework.example.validation.validations.UserValidation;
import colesico.framework.example.validation.validations.UserValidationBrief;

@ValidatorBuilder(superclass = UserValidation.class, subject = "user", command = "series")
@ValidatorBuilder(superclass = UserValidationBrief.class, isDefault = false, subject = "user", command = "series")
public class User {

    @Validate(UserValidation.class)
    @Validate(UserValidationBrief.class)
    private Long id;

    @Validate(UserValidation.class)
    @Validate(UserValidationBrief.class)
    private String name;

    @BeanValidate(UserValidation.class)
    @BeanValidate(UserValidationBrief.class)
    private Credentials credentials;

    @BeanValidate
    private Contacts contacts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
