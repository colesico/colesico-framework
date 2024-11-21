package colesico.framework.example.jdbirec;


import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.ColumnOverriding;
import colesico.framework.jdbirec.Composition;
import colesico.framework.jdbirec.RecordView;

public class User {
    @Column()
    private Number id;

    @Column(name = "person")
    private String name;

    @Composition(columnOverriding = @ColumnOverriding(column = "phone", name = "phn"))
    private Contacts home;

    @Composition(views = RecordView.FULL_RECORD, renaming = "wrk_@column")
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
