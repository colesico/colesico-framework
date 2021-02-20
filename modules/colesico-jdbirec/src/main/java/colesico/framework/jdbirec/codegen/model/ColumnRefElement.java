package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

public class ColumnRefElement {

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

    public ColumnRefElement(String column) {
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
}
