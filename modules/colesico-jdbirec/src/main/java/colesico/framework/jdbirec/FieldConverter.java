package colesico.framework.jdbirec;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface FieldConverter<F, S> {

    String TO_FIELD_METHOD = "toField";
    String FROM_FIELD_METHOD = "fromField";

    F toField(String columnName, ResultSet rs) throws SQLException;

    S fromField(F fieldValue);
}
