package colesico.framework.dao.codegen.model;

import org.apache.commons.lang3.StringUtils;

public class ColumnElement {

    protected TableElement parentTable;

    /**
     * Predefined name
     */
    protected String name;

    /**
     * Predefined sql type
     */
    protected String type;

    /**
     * Is column value required or not
     */
    protected boolean required;

    /**
     * Column comment/description
     */
    protected String comment;

    /**
     * Column default value
     */
    protected String defaultVal;

    protected String uid;

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public TableElement getParentTable() {
        return parentTable;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public String getComment() {
        return comment;
    }


    public void setName(String name) {
        this.name = name;
        this.uid = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setParentTable(TableElement parentTable) {
        this.parentTable = parentTable;
    }

    public <C extends ColumnElement> void updateWith(C column) {
        if (StringUtils.isNotEmpty(column.getType())) {
            type = column.getType();
        }

        if (StringUtils.isNotEmpty(column.getComment())) {
            comment = column.getComment();
        }

        if (StringUtils.isNotEmpty(column.getDefaultVal())) {
            defaultVal = column.getDefaultVal();
        }

        if (!required && column.isRequired()) {
            required = column.isRequired();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldColumnElement that = (FieldColumnElement) o;

        return uid.equals(that.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }

}
