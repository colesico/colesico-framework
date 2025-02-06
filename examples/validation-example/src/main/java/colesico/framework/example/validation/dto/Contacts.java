package colesico.framework.example.validation.dto;

import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.ValidatorBuilder;

@ValidatorBuilder
public class Contacts {

    @Validate
    private PostAddress postAddress;

    @Validate(verifier = true)
    private String phone;

    @Validate
    private String email;

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
}
