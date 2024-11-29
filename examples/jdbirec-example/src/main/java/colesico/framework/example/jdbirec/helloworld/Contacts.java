package colesico.framework.example.jdbirec.helloworld;

import colesico.framework.jdbirec.Column;

public class Contacts {

    @Column
    private String phone;

    @Column
    private String address;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
