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
public class VUser {

    @Column
    private Integer id;

    @Column(tags = TG_BRIEF)
    private String name = null;

    @Composition(renaming = "h_@column", tags = TG_FULL)
    private VContacts home;

    @Composition(renaming = "w_@column", tags = {TG_FULL, TG_BRIEF})
    private VContacts work;

    @Composition(renaming = RN_PREFIX,
            tagFilter = @TagFilter(anyOf = {TG_FULL, "#extra.email"})
    )
    private VContacts extra;

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

    public VContacts getHome() {
        return home;
    }

    public void setHome(VContacts home) {
        this.home = home;
    }

    public VContacts getWork() {
        return work;
    }

    public void setWork(VContacts work) {
        this.work = work;
    }

    public VContacts getExtra() {
        return extra;
    }

    public void setExtra(VContacts extra) {
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
