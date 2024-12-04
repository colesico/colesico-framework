package colesico.framework.example.jdbirec.join;

import colesico.framework.jdbirec.Column;

public class JContacts {

    @Column
    private String phone;

    @Column
    private String email;

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
