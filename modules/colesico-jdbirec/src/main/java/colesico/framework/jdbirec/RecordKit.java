package colesico.framework.jdbirec;

import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

abstract public class RecordKit<R> {

    public static final String INIT_COMPOSITION_METHOD = "initCompositions";
    public static final String EXPORT_METOD = "exportTo";
    public static final String IMPORT_METHOD = "importFrom";
    public static final String TABLE_NAME_METHOD = "tableName";
    public static final String SQL_INSERT_METHOD = "sqlInsert";
    public static final String SQL_UPDATE_METHOD = "sqlUpdate";
    public static final String SQL_SELECT_METHOD = "sqlSelect";
    public static final String NEW_RECORD_METHOD = "newRecord";

    public static final String RECORD_PARAM = "record";
    public static final String RECEIVER_PARAM = "receiver";
    public static final String RESULT_SET_PARAM = "rs";
    public static final String QUALIFICATION_PARAM = "qualification";

    abstract public void initCompositions(R record);

    abstract public void exportTo(R record, ValueReceiver receiver);

    abstract public R importFrom(R record, ResultSet rs) throws SQLException;

    /**
     * Return table name
     *
     * @return
     */
    abstract public String tableName();

    /**
     * Return insert sql query text
     *
     * @return
     */
    abstract public String sqlInsert();

    abstract public String sqlUpdate(String qualification);

    abstract public String sqlSelect(String qualification);

    abstract public R newRecord();

    public final Map<String, Object> map(R record) {
        final Map<String, Object> result = new HashMap<>();
        exportTo(record, (c, v) -> result.put(c, v));
        return result;
    }

    public final RowMapper<R> mapper() {
        return (rs, ctx) -> importFrom(newRecord(), rs);
    }

    @FunctionalInterface
    public interface ValueReceiver {

        String RECEIVE_METHOD = "receive";
        String COLUMN_PARAM = "column";
        String VALUE_PARAM = "value";

        void receive(String column, Object value);
    }

}
