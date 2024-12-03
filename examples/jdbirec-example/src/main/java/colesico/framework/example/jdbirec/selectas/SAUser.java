package colesico.framework.example.jdbirec.selectas;


import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.Composition;
import colesico.framework.jdbirec.Record;

import static colesico.framework.jdbirec.Composition.RN_PREFIX;

@Record
public class SAUser {

    @Column
    private Integer id;

    @Column(selectAs = "'USER:' || @column")
    private String name;

    @Composition(name = "h", renaming = RN_PREFIX)
    private Contacts contacts;

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

    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }
}
