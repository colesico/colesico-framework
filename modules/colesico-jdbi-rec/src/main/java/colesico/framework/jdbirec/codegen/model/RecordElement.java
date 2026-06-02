package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

import java.util.HashSet;
import java.util.Set;

/**
 * @see colesico.framework.jdbirec.Record
 */
public class RecordElement {
    /**
     * Parent record kit
     */
    private final RecordKitElement recordKit;

    /**
     * Record type
     */
    private final ClassType type;

    /**
     * Master table name associated with record of given type
     */
    private final String tableName;

    private final String tableAlias;

    /**
     * Root compositions derived from record  class
     */
    private final Set<RecordViewElement> views = new HashSet<>();


    public RecordElement(RecordKitElement recordKit, ClassType type, String tableName, String tableAlias) {
        this.recordKit = recordKit;
        this.type = type;
        this.tableName = tableName;
        this.tableAlias = tableAlias;
    }

    public void addView(RecordViewElement view) {
        if (!views.add(view)) {
            throw new RuntimeException("Duplicate record view: " + view.getName());
        }
    }

    public RecordKitElement getRecordKit() {
        return recordKit;
    }

    public ClassType getType() {
        return type;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public Set<RecordViewElement> getViews() {
        return views;
    }
}
