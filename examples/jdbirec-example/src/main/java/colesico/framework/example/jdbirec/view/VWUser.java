package colesico.framework.example.jdbirec.view;


import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.Composition;
import colesico.framework.jdbirec.Record;
import colesico.framework.jdbirec.TagFilter;

import static colesico.framework.jdbirec.Composition.RN_PREFIX;
import static colesico.framework.jdbirec.Record.VIEW_BRIEF;
import static colesico.framework.jdbirec.Record.VIEW_FULL;
import static colesico.framework.jdbirec.TagFilter.*;

@Record
@Record(view = VIEW_FULL, tagFilter = @TagFilter(anyOf = {TG_FULL, TF_NO_TAGS}))
@Record(view = VIEW_BRIEF, tagFilter = @TagFilter(anyOf = {TG_BRIEF, TF_NO_TAGS}))
public class VWUser {

    @Column
    private Integer id;

    @Column(tags = TG_BRIEF)
    private String name = null;

    @Composition(renaming = "h_@column", tags = TG_FULL)
    private Contacts home;

    @Composition(renaming = "w_@column", tags = {TG_FULL, TG_BRIEF})
    private Contacts work;

    @Composition(renaming = RN_PREFIX,
            tagFilter = @TagFilter(anyOf = {TG_FULL, "#extra.email"})
    )
    private Contacts extra;

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

    public Contacts getExtra() {
        return extra;
    }

    public void setExtra(Contacts extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", home=" + home +
                ", work=" + work +
                ", extra=" + extra +
                '}';
    }
}
