package colesico.framework.example.jdbirec.view;


import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.Composition;
import colesico.framework.jdbirec.Record;
import colesico.framework.jdbirec.TagFilter;

import static colesico.framework.jdbirec.Record.VIEW_BRIEF;
import static colesico.framework.jdbirec.Record.VIEW_FULL;
import static colesico.framework.jdbirec.TagFilter.*;

@Record(view = VIEW_FULL, tagFilter = @TagFilter(anyOf = {TG_FULL, TF_NO_TAGS}))
@Record(view = VIEW_BRIEF, tagFilter = @TagFilter(anyOf = {TG_BRIEF, TF_NO_TAGS}))
public class User {

    @Column
    private Number id;

    @Column
    private String name;

    @Composition(renaming = "h_@column", tags = TG_FULL)
    private Contacts home;

    @Composition(renaming = "w_@column", tags = {TG_FULL, TG_BRIEF})
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
