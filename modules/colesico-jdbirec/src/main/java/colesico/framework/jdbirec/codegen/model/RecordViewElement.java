package colesico.framework.jdbirec.codegen.model;

import colesico.framework.jdbirec.TagFilter;

import java.util.Objects;

public class RecordViewElement {
    private final String name;
    private final TagFilterElement tagFilter;

    public RecordViewElement(String name, TagFilterElement tagFilter) {
        this.name = name;
        this.tagFilter = tagFilter;
    }

    public String getName() {
        return name;
    }

    public TagFilterElement getTagFilter() {
        return tagFilter;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecordViewElement that = (RecordViewElement) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
