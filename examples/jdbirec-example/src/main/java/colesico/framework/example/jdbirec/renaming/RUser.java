package colesico.framework.example.jdbirec.renaming;


import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.ColumnOverriding;
import colesico.framework.jdbirec.Composition;
import colesico.framework.jdbirec.Record;

import static colesico.framework.jdbirec.Composition.RN_PREFIX;

@Record(
        columnOverriding = @ColumnOverriding(column = "id", name = "user_id")
)
public class RUser {
    @Column
    private Integer id;

    @Column(name = "user_name")
    private String name;

    @Composition(renaming = "h_@column",
            columnOverriding = {
                    @ColumnOverriding(column = "phone", name = "mobile_number"),
                    @ColumnOverriding(column = "location", name = "post_addr")
            }
    )
    private RContacts home;

    @Composition(name = "w", renaming = RN_PREFIX)
    private RContacts work;

    public Number getId() {
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

    public RContacts getHome() {
        return home;
    }

    public void setHome(RContacts home) {
        this.home = home;
    }

    public RContacts getWork() {
        return work;
    }

    public void setWork(RContacts work) {
        this.work = work;
    }
}
