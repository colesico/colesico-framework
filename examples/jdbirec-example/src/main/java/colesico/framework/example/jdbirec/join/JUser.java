package colesico.framework.example.jdbirec.join;


import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.Composition;
import colesico.framework.jdbirec.Record;

@Record
public class JUser {

    @Column
    private Integer id;

    @Column
    private String name;

    @Composition
    private JContacts contacts;

    @Composition
    private JContacts joinContacts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JContacts getContacts() {
        return contacts;
    }

    public void setContacts(JContacts contacts) {
        this.contacts = contacts;
    }

    public JContacts getJoinContacts() {
        return joinContacts;
    }

    public void setJoinContacts(JContacts joinContacts) {
        this.joinContacts = joinContacts;
    }
}
