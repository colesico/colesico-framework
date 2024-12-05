package colesico.framework.example.jdbirec.join;

import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.Record;

@Record(table = "j_contacts")
public class JContacts {

    @Column
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
