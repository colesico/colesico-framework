package colesico.framework.example.jdbirec.selectas;


import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.Composition;
import colesico.framework.jdbirec.Record;

import static colesico.framework.jdbirec.Column.AS_NOP;

@Record(table = "v_users", tableAlias = "usr")
public class SUser {

    @Column
    private Integer id;

    // column selection to be defined in select statement
    @Column(exportable = false, selectAs = AS_NOP, insertAs = AS_NOP)
    private Integer count;

    @Column(selectAs = "'USER:' || @column")
    private String name;

    @Composition(renaming = "h_@column")
    private SContacts contacts;

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

    public SContacts getContacts() {
        return contacts;
    }

    public void setContacts(SContacts contacts) {
        this.contacts = contacts;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
