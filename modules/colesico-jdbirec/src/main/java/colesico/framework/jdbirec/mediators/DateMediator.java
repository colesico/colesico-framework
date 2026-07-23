package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.AbstRactrecordKit;
import colesico.framework.jdbirec.FieldMediator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;

public class DateMediator implements FieldMediator<Date> {

    @Override
    public Date importField(String columnName, ResultSet rs) throws SQLException {
        String dateStr = rs.getString(columnName);
        // Checks if the string is null, empty, or contains only whitespace
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        // Parses ISO 8601 string (e.g., "2026-07-23T08:37:00Z") to Instant and converts to Date
        return Date.from(Instant.parse(dateStr));
    }

    @Override
    public void exportField(Date date, String fieldName, AbstRactrecordKit.FieldReceiver fr) {
        // Converts Date to Instant, which automatically formats to ISO 8601 with trailing 'Z'
        fr.set(fieldName, date == null ? null : date.toInstant().toString());
    }
}