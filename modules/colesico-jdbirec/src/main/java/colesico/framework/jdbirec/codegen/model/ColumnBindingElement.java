package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

public class ColumnBindingElement {

    /**
     * Composition column name
     */
    private final String column;

    /**
     * Column name overriding
     */
    private String name;

    /**
     * Mediator overriding
     */
    private ClassType mediator;

    /**
     * Indicate that this binding element was associated with a column element
     */
    private boolean associated = false;

    public ColumnBindingElement(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassType getMediator() {
        return mediator;
    }

    public void setMediator(ClassType mediator) {
        this.mediator = mediator;
    }

    public boolean isAssociated() {
        return associated;
    }

    public void setAssociated(boolean associated) {
        this.associated = associated;
    }

    @Override
    public String toString() {
        return "ColumnBindingElement{" +
                "column='" + column + '\'' +
                ", name='" + name + '\'' +
                ", mediator=" + mediator +
                ", associated=" + associated +
                '}';
    }
}
