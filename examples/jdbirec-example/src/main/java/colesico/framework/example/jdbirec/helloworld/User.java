package colesico.framework.example.jdbirec.helloworld;


import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.Composition;
import colesico.framework.jdbirec.Record;

import static colesico.framework.jdbirec.Composition.RN_PREFIX;

@Record(table = "users")
public class User {
    @Column
    private Number id;

    @Column
    private String name;

    @Composition
    private Contacts home;

    @Composition(renaming = RN_PREFIX)
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
