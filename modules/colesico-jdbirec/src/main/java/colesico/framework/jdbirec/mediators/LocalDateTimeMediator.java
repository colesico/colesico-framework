package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.AbstRactrecordKit;
import colesico.framework.jdbirec.FieldMediator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeMediator implements FieldMediator<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime importField(String columnName, ResultSet rs) throws SQLException {
        String dateStr = rs.getString(columnName);
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateStr.trim(), FORMATTER);
    }

    @Override
    public void exportField(LocalDateTime dateTime, String fieldName, AbstRactrecordKit.FieldReceiver fr) {
        fr.set(fieldName, dateTime == null ? null : dateTime.format(FORMATTER));
    }
}