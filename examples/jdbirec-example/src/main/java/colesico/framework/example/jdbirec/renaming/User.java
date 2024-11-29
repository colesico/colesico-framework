package colesico.framework.example.jdbirec.renaming;


import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.ColumnOverriding;
import colesico.framework.jdbirec.Composition;
import colesico.framework.jdbirec.Record;

import static colesico.framework.jdbirec.Composition.RN_PREFIX;

@Record(
        columnOverriding = @ColumnOverriding(column = "id", name = "user_id")
)
public class User {
    @Column
    private Number id;

    @Column(name = "user_name")
    private String name;

    @Composition(renaming = "h_@column",
            columnOverriding = {
                    @ColumnOverriding(column = "phone", name = "mobile_number"),
                    @ColumnOverriding(column = "location", name = "post_addr")
            }
    )
    private Contacts home;

    @Composition(name = "w", renaming = RN_PREFIX)
    private Contacts work;

    public Number getId() {
        return id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contacts getHome() {
        return home;
    }

    public void setHome(Contacts home) {
        this.home = home;
    }

    public Contacts getWork() {
        return work;
    }

    public void setWork(Contacts work) {
        this.work = work;
    }
}
