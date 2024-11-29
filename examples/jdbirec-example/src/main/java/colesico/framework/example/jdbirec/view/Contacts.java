package colesico.framework.example.jdbirec.view;

import colesico.framework.jdbirec.Column;

import static colesico.framework.jdbirec.TagFilter.TG_FULL;

public class Contacts {

    @Column
    private String phone;

    @Column(tags = TG_FULL)
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
