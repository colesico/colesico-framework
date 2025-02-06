package colesico.framework.example.validation.dto;

import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.ValidatorBuilder;

@ValidatorBuilder
public class PostAddress {

    @Validate
    private String postCode;

    @Validate
    private String address;

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
