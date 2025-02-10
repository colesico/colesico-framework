package colesico.framework.example.validation.dto;

import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.ValidateBean;
import colesico.framework.beanvalidation.ValidatorBuilder;
import colesico.framework.example.validation.validations.AppValidatorBuilder;

@ValidatorBuilder(superclass = AppValidatorBuilder.class, command = "series")
public class User {

    @Validate
    private Long id;

    @Validate
    private String name;

    @ValidateBean
    private Credentials credentials;

    @ValidateBean
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
