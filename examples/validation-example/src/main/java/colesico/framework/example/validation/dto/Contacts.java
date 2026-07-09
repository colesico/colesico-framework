package colesico.framework.example.validation.dto;

import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.BeanValidate;
import colesico.framework.beanvalidation.ValidatorBuilder;
import colesico.framework.example.validation.validations.ContactsValidation;

@ValidatorBuilder(ContactsValidation.class)
public class Contacts {

    @Validate
    private PostAddress postAddress;

    @Validate(verifier = true)
    private String phone;

    @Validate
    private String email;

    @BeanValidate(target = "brief")
    private Credentials credentials;

    public PostAddress getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(PostAddress postAddress) {
        this.postAddress = postAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
