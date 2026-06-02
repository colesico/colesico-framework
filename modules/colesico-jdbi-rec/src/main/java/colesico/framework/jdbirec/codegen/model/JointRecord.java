package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

/**
 * Joint record  to use in queries with table joins
 */
public class JointRecord {

    private final String tableName;

    private final ClassType recordType;

    public JointRecord(String tableName, ClassType recordType) {
        this.tableName = tableName;
        this.recordType = recordType;
    }

    public String getTableName() {
        return tableName;
    }

    public ClassType getRecordType() {
        return recordType;
    }

}
