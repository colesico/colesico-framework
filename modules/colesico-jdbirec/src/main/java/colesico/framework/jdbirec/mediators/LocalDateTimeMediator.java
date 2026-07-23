package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.AbstRactrecordKit;
import colesico.framework.jdbirec.FieldMediator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LocalDateTimeMediator implements FieldMediator<LocalDateTime> {

    @Override
    public LocalDateTime importField(String columnName, ResultSet rs) throws SQLException {
        String dateStr = rs.getString(columnName);
        // Checks if the string is null, empty, or contains only whitespace
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        // Parses local ISO 8601 string without timezone (e.g., "2026-07-23T11:40:00")
        return LocalDateTime.parse(dateStr);
    }

    @Override
    public void exportField(LocalDateTime dateTime, String fieldName, AbstRactrecordKit.FieldReceiver fr) {
        // Converts LocalDateTime to local ISO 8601 string format without zone indicators
        fr.set(fieldName, dateTime == null ? null : dateTime.toString());
    }
}
