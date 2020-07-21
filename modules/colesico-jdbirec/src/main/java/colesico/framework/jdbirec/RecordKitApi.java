package colesico.framework.jdbirec;

import org.jdbi.v3.core.mapper.RowMapper;

import java.util.Map;

public interface RecordKitApi<R> {

    String TABLE_NAME_REF = "@table";
    String RECORD_REF = "@record";
    String COLUMNS_REF = "@columns";
    String UPDATES_REF = "@updates";
    String VALUES_REF = "@values";

    R newRecord();

    /**
     * Return table name
     *
     * @return
     */
    String getTableName();

    /**
     * Transforms query text with references (@table, @columns, @updates, @values) to actual sql
     */
    String sql(String query);

    Map<String, Object> map(R record);

    RowMapper<R> mapper();

    @FunctionalInterface
    public interface FieldReceiver {

        String SET_METHOD = "set";
        String FIELD_PARAM = "field";
        String VALUE_PARAM = "value";

        void set(String field, Object value);
    }
}
