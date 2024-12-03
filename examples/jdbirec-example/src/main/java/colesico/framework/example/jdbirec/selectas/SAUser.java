package colesico.framework.example.jdbirec.selectas;


import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.Composition;
import colesico.framework.jdbirec.Record;

import static colesico.framework.jdbirec.Column.NOP_REF;

@Record
public class SAUser {

    @Column
    private Integer id;

    // column selection to be defined in select statement
    @Column(exportable = false, selectAs = NOP_REF, updateAs = NOP_REF, insertAs = NOP_REF)
    private Integer count;

    @Column(selectAs = "'USER:' || @column")
    private String name;

    @Composition(renaming = "h_@column")
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
