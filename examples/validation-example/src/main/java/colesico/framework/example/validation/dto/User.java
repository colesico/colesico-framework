package colesico.framework.example.validation.dto;

import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.ValidateBean;
import colesico.framework.beanvalidation.ValidatorBuilderPrototype;
import colesico.framework.example.validation.validations.AppValidatorBuilder;

@ValidatorBuilderPrototype(superclass = AppValidatorBuilder.class, command = "series")
@ValidatorBuilderPrototype(name = "brief", superclass = AppValidatorBuilder.class, command = "series")
public class User {

    @Validate(builders = {"default", "brief"})
    private Long id;

    @Validate(builders = {"default", "brief"})
    private String name;

    @ValidateBean(builders = {"default", "brief"})
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
