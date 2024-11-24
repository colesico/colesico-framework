package colesico.framework.jdbirec.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents {@link colesico.framework.jdbirec.TagFilter}
 */
public class TagFilterElement {

    private List<String> oneOf = new ArrayList<>();
    private List<String> anyOf = new ArrayList<>();
    private List<String> noneOf = new ArrayList<>();


    public List<String> getOneOf() {
        return oneOf;
    }

    public void setOneOf(List<String> oneOf) {
        this.oneOf = oneOf;
    }

    public List<String> getAnyOf() {
        return anyOf;
    }

    public void setAnyOf(List<String> anyOf) {
        this.anyOf = anyOf;
    }

    public List<String> getNoneOf() {
        return noneOf;
    }

    public void setNoneOf(List<String> noneOf) {
        this.noneOf = noneOf;
    }
}
