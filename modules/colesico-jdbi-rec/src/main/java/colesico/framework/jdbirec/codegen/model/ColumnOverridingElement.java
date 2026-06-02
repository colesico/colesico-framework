package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.jdbirec.ColumnOverriding;

public class ColumnOverridingElement {

    /**
     * Column name overriding
     *
     * @see ColumnOverriding#name()
     */
    private String name;

    /**
     * Column definition overriding
     *
     * @see ColumnOverriding#definition()
     */
    private String definition;

    /**
     * Mediator overriding
     *
     * @see ColumnOverriding#mediator()
     */
    private ClassType mediator;

    /**
     * Target composition column path to be overriding
     *
     * @see ColumnOverriding#column()
     */
    private final String columnPath;

    /**
     * Indicate that this overriding element was associated with a column element.
     * I.e. parser actually founds column element for overriding
     */
    private boolean associated = false;

    public ColumnOverridingElement(String columnPath) {
        this.columnPath = columnPath;
    }

    public String getColumnPath() {
        return columnPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
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
        return "ColumnOverridingElement{" +
                "columnPath='" + columnPath + '\'' +
                ", name='" + name + '\'' +
                ", definition='" + definition + '\'' +
                ", mediator=" + mediator +
                ", associated=" + associated +
                '}';
    }
}
