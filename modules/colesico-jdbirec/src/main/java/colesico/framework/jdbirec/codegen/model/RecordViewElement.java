package colesico.framework.jdbirec.codegen.model;

import java.util.Objects;

public class RecordViewElement {
    private final String name;
    private final String tagFilter;

    public RecordViewElement(String name, String tagFilter) {
        this.name = name;
        this.tagFilter = tagFilter;
    }

    public String getName() {
        return name;
    }

    public String getTagFilter() {
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
