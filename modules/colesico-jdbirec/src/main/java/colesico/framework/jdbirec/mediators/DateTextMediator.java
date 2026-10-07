package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.AbstRactrecordKit;
import colesico.framework.jdbirec.FieldMediator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTextMediator implements FieldMediator<Date> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @Override
    public Date importField(String columnName, ResultSet rs) throws SQLException {
        String dateStr = rs.getString(columnName);
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        Instant instant = FORMATTER.parse(dateStr.trim(), Instant::from);
        return Date.from(instant);
    }

    @Override
    public void exportField(Date date, String fieldName, AbstRactrecordKit.FieldReceiver fr) {
        fr.set(fieldName, date == null ? null : FORMATTER.format(date.toInstant()));
    }
}