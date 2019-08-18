package colesico.framework.jdbirec;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Is used to customize database columns values to field value transforming and back.
 *
 * @param <F>
 */
public interface FieldMediator<F> {

    String IMPORT_METHOD = "importField";
    String EXPORT_METHOD = "exportField";

    /**
     * Import field values from result set and return field value object
     *
     * @param columnName basic column name from @Column.name() annotation
     * @param rs
     * @return
     * @throws SQLException
     */
    F importField(String columnName, ResultSet rs) throws SQLException;

    /**
     * Export field value to the column  assigner.
     * Sending process may hit one or more database columns
     *
     * @param fieldValue
     * @param columnName column name from @Column.name() annotation
     * @param vr         value receiver
     */
    void exportField(F fieldValue, String columnName, RecordKit.ColumnAssigner vr);

}
