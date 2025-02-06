package colesico.framework.example.validation.dto;

public class Contacts {

    private PostAddress postAddress;
    private String phone;
    private String email;

    public PostAddress getAddress() {
        return postAddress;
    }

    public void setAddress(PostAddress postAddress) {
        this.postAddress = postAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
