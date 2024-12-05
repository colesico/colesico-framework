package colesico.framework.example.jdbirec.join;


import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.Composition;
import colesico.framework.jdbirec.Record;
import colesico.framework.jdbirec.TagFilter;

@Record(table = "j_users", tableAlias = "usr")
public class JUser {

    @Column
    private Integer id;

    @Column
    private String name;

    @Composition(join = true, tagFilter = @TagFilter(noneOf = "#contacts.id"))
    private JContacts contacts;

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
}
